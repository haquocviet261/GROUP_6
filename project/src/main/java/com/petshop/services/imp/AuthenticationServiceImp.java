package com.petshop.services.imp;


import com.petshop.common.constant.Role;
import com.petshop.common.constant.TokenType;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImp implements AuthenticationService {
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

}
