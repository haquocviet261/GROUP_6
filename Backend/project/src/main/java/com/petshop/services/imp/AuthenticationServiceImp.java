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
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

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
    @Autowired
    private JwtDecoder jwtDecoder;
    @Autowired
    HttpServletRequest request;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private  String CLIENT_ID ;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private  String CLIENT_SECERT;
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
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getUser_id());
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
                else if (request.getPhonenumber().equals(list.get(i).getPhone_number())) {
                    return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObject(Validation.FAIL,"Phone number are exist !",""));
                }
            }
        }
        var user = User.builder().user_name(request.getUsername()).first_name(request.getFirstname())
                .last_name(request.getLastname()).email(request.getEmail()).address(request.getAddress())
                .phone_number(request.getPhonenumber()).password(passwordEncoder.encode(request.getPassword())).role(Role.customer).status(1)
                .date_of_birth(request.getDateofbirth())
                .build();
        var savedUser = userRepository.save(user);
        var jwtToken = jwtServiceImp.generateToken(user);
        var refreshToken = jwtServiceImp.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(Validation.OK,"Register successfully !",AuthenticationResponse.builder().accessToken(jwtToken).refresh_token(refreshToken).build()));
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
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(Validation.OK,"Login successfully !",AuthenticationResponse.builder().accessToken(jwt).refresh_token(refreshToken).build()));
    }
    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
                        .refresh_token(refreshToken)
                        .build();
            }
        }
    }
    public ResponseEntity<ResponseObject> extracUser(){
        User user;
        String jwt;
        String refreshToken;
            String header = request.getHeader("Authorization");
        Map<String,Object> userInfor = extractUserInfoFromToken(header.substring(7));
                Optional<User> checkMailExist = userRepository.findByEmail(userInfor.get("email").toString().trim());
            if (checkMailExist.isPresent()){
                user = checkMailExist.get();
                jwt = jwtServiceImp.generateToken(user);
                refreshToken = jwtServiceImp.generateRefreshToken(user);
                return ResponseEntity.ok(new ResponseObject("OK","Handle authorization code and state successfully!",AuthenticationResponse.builder().accessToken(jwt).refresh_token(refreshToken).build()));
            }else {
                String email =  userInfor.get("email").toString().trim();
                user = User.builder().email(userInfor.get("email").toString().trim())
                        .user_name(userInfor.get("email").toString().trim().split("@")[0])
                        .role(Role.customer)
                        .status(1)
                        .email(email)
                        .password(passwordEncoder.encode(PasswordGenerator.generateRandomPassword(8)))
                        .first_name(userInfor.get("family_name").toString().trim())
                        .last_name(userInfor.get("given_name").toString().trim())
                        .image_src(userInfor.get("picture").toString())
                        .build();
                userRepository.save(user);
                jwt = jwtServiceImp.generateToken(user);
                refreshToken = jwtServiceImp.generateRefreshToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, jwt);
            }

            return ResponseEntity.ok(new ResponseObject("OK","Handle authorization code and state successfully!",AuthenticationResponse.builder().accessToken(jwt).refresh_token(refreshToken).build()));
    }

    public Map<String, Object> extractUserInfoFromToken(String accessToken) {
        Jwt jwt = jwtDecoder.decode(accessToken);
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("email", jwt.getClaim("email"));
        userInfo.put("picture", jwt.getClaim("picture"));
        userInfo.put("given_name", jwt.getClaim("given_name"));
        userInfo.put("family_name", jwt.getClaim("family_name"));
        userInfo.put("iat", jwt.getClaim("iat"));
        userInfo.put("exp", jwt.getClaim("exp"));
        return userInfo;
    }
}
