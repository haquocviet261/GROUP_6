package com.petshop.services.imp;


import com.petshop.common.constant.Role;
import com.petshop.common.constant.TokenType;
import com.petshop.common.utils.PasswordGenerator;
import com.petshop.models.dto.request.UserDto;
import com.petshop.models.dto.request.RegisterRequest;
import com.petshop.models.dto.response.AuthenticationResponse;

import com.petshop.models.dto.response.ResponseObject;

import com.petshop.models.entities.Token;
import com.petshop.models.entities.User;
import com.petshop.repositories.TokenRepository;
import com.petshop.repositories.UserRepository;
import com.petshop.common.utils.Validation;
import com.petshop.services.interfaces.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImp implements AuthenticationService {
    @Autowired
    private RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final JwtServiceImp jwtServiceImp;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager  authenticationManager;
    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }
    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getUserId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
    @Override
    public ResponseEntity<ResponseObject> register(RegisterRequest request) {
        List<User> list = userRepository.findAll();
        for (int i = 0; i < list.size() ; i++) {
            if (list.get(i).getUsername().equals(request.getUsername())){
                return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObject(Validation.FAIL,"User Name are exist !",""));
            }else {
                if (list.get(i).getEmail().equals(request.getEmail())) {
                    return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObject(Validation.FAIL,"Email are exist !",""));
                }
                else if (request.getPhonenumber().equals(list.get(i).getPhoneNumber())) {
                    return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObject(Validation.FAIL,"Phone number are exist !",""));
                }
            }
        }
        var user = User.builder().UserName(request.getUsername()).FirstName(request.getFirstname())
                .LastName(request.getLastname()).Email(request.getEmail()).Address(request.getAddress())
                .PhoneNumber(request.getPhonenumber()).Password(passwordEncoder.encode(request.getPassword())).Role(Role.customer).Status(1)
                .DateOfBirth(request.getDateofbirth())
                .build();
        var savedUser = userRepository.save(user);
        var jwtToken = jwtServiceImp.generateToken(user);
        var refreshToken = jwtServiceImp.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(Validation.OK,"Register successfully !",AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build()));
    }
    @Override
    public ResponseEntity<ResponseObject> authenticated(UserDto request){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));
        var user = userRepository.findByUserName(request.getUsername()).orElseThrow();
        if (user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(Validation.FAIL,"User Name are incorrect !",""));
        }else if (user.getPassword().equals(request.getPassword())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(Validation.FAIL,"Password are incorrect !",""));
        }
        var jwt = jwtServiceImp.generateToken(user);
        var refreshToken = jwtServiceImp.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwt);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(Validation.OK,"Login successfully !",AuthenticationResponse.builder().accessToken(jwt).refreshToken(refreshToken).build()));
    }
    @Override
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userName;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userName = jwtServiceImp.extractUsername(refreshToken);
        if (userName != null) {
            var user = this.userRepository.findByUserName(userName)
                    .orElseThrow();
            if (jwtServiceImp.isTokenValid(refreshToken, user)) {
                var accessToken = jwtServiceImp.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
            }
        }
    }
    public ResponseEntity<ResponseObject> handleGoogleCallback(String authorizationCode){
        String tokenEndpoint = "https://www.googleapis.com/oauth2/v4/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", authorizationCode);
        body.add("client_id", "756081284225-qk5ijqli6cuope3q2j3mdlm2ckgtvedb.apps.googleusercontent.com");
        body.add("client_secret", "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");
        body.add("redirect_uri", "http://localhost:9999/login/oauth2/code/google");
        body.add("grant_type", "authorization_code");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.exchange(tokenEndpoint, HttpMethod.POST, request, Map.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> tokenInfo = response.getBody();
            System.out.println("Access Token: " + tokenInfo.get("access_token"));

            String userInfoEndpoint = "https://www.googleapis.com/oauth2/v3/userinfo";
            HttpHeaders userInfoHeaders = new HttpHeaders();
            userInfoHeaders.setBearerAuth((String) tokenInfo.get("access_token"));
            HttpEntity<Void> userInfoRequest = new HttpEntity<>(userInfoHeaders);
            ResponseEntity<Map> userInfoResponse = restTemplate.exchange(userInfoEndpoint, HttpMethod.GET, userInfoRequest, Map.class);
            if (userInfoResponse.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> userInfo = userInfoResponse.getBody();
                String email = (String) userInfo.get("email");
                Optional<User> checkMailExist = userRepository.findByEmail(email);
                if (checkMailExist.isPresent()){
                    User u = checkMailExist.get();
                    var jwtToken = jwtServiceImp.generateToken(u);
                    var refreshToken = jwtServiceImp.generateRefreshToken(u);
                    saveUserToken(u, jwtToken);
                    return ResponseEntity.ok(new ResponseObject("OK","Handle authorization code and state successfully!",new AuthenticationResponse(jwtToken,refreshToken)));
                }
                User user = User.builder().Email(email)
                        .UserName(email.split("@")[0])
                        .Role(Role.customer)
                        .Status(1)
                        .Password(passwordEncoder.encode(PasswordGenerator.generateRandomPassword(8)))
                        .FirstName(userInfo.get("family_name").toString())
                        .LastName(userInfo.get("given_name").toString()).build();
                userRepository.save(user);
                return ResponseEntity.ok(new ResponseObject("OK","Handle authorization code and state successfully!",userInfo));
            }
        }
        return ResponseEntity.ok(new ResponseObject("OK","Failed to handle authorization code and state.","{}"));

    }
}
