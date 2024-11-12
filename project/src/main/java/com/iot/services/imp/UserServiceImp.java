package com.iot.services.imp;

import com.iot.common.constant.CommonConstant;
import com.iot.common.utils.*;
import com.iot.model.dto.request.*;
import com.iot.model.dto.response.AuthenticationResponse;
import com.iot.model.dto.response.ResponseObject;
import com.iot.model.entity.Token;
import com.iot.model.entity.User;
import com.iot.repositories.TokenRepository;
import com.iot.repositories.UserRepository;
import com.iot.services.interfaces.UserService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtServiceImp jwtServiceImp;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtDecoder jwtDecoder;
    @Autowired
    HttpServletRequest request;
    @Autowired
    private EmailUtils emailUtil;

    @Override
    public ResponseEntity<ResponseObject> changePassword(ChangePasswordRequest request) {
        User user = CommonUtils.getUserInformationLogin();
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            return ResponseEntity.ok(new ResponseObject(Validation.FAIL, "The current password you entered is incorrect. Please try again.", null));
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return ResponseEntity.ok(new ResponseObject(Validation.OK, "Change Password successfully !", null));
    }

    @Override
    public ResponseEntity<ResponseObject> getAllUsers() {
        User user = CommonUtils.getUserInformationLogin();
        if (user.getRole().equals(CommonConstant.ADMIN)) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject(Validation.OK, "Retrieved users successfully", userRepository.getAllUserForAdmin()));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(Validation.OK, "Retrieved users successfully"
                        , userRepository.getAllUserByCompanyExceptManager(user.getCompany_id())));
    }

    public ResponseEntity<ResponseObject> deleteUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(Validation.FAIL, "userId" + id + " is not exist", ""));
        }
        User user = optionalUser.get();
        user.setDeleted_at(new Date());
        user.setStatus("INACTIVE");
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(Validation.OK, "Deleted User" + id + " successfully!", userRepository.save(user)));
    }

    @Override
    public ResponseEntity<ResponseObject> editUser(EditUserDTO editUserDTO) {
        User user = CommonUtils.getUserInformationLogin();
        BeanUtils.copyProperties(editUserDTO, user);
        user.setUpdated_by(user.getUser_name());
        return ResponseEntity.ok(new ResponseObject(Validation.OK, "Updated successfully!", userRepository.save(user)));
    }

    @Override
    public ResponseEntity<ResponseObject> resetPassword(ForgotPasswordRequest request) throws MessagingException {
        String email = request.getEmail();
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.ok(new ResponseObject(Validation.FAIL, "Email " + email + " not found !", ""));
        }
        User user = optionalUser.get();
        String newPassword = PasswordGenerator.generateRandomPassword(8);
        emailUtil.sendEmailToResetPassword(email, user.getUser_name(), newPassword);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return ResponseEntity.ok(new ResponseObject(Validation.OK, "Password reset successful! Check your email for details.", ""));
    }

    public ResponseEntity<String> registerAdmin(RegisterRequest request) {
        String email = request.getEmail();
        User user = new User();
        user.setRole(CommonConstant.ADMIN);
        user.setUser_name(email.split("@")[0]);
        user.setPassword(passwordEncoder.encode("11122002"));
        user.setEmail(email);
        user.setStatus("ACTIVE");
        userRepository.save(user);
        return ResponseEntity.ok("Ok!!!!");
    }

    @Override
    public ResponseEntity<ResponseObject> register(RegisterRequest request) throws MessagingException {
        User user = CommonUtils.getUserInformationLogin();
        String email = request.getEmail();
        Optional<User> optionalUser = userRepository.findByEmailForRegister(email);
        if (optionalUser.isEmpty()) {
            //Create new User with Status is INACTIVE
            User newUser = new User();
            newUser.setUser_name(generateUsername(email));
            newUser.setPassword("temporary-password");
            newUser.setEmail(email);
            if (user.getRole().equals(CommonConstant.ADMIN)) {
                newUser.setRole(CommonConstant.MANAGER);
            } else {
                newUser.setRole(CommonConstant.STAFF);
            }
            newUser.setStatus("INACTIVE");
            newUser.setCompany_id(user.getCompany_id());
            userRepository.save(newUser);

            String token = jwtServiceImp.generateToken(newUser);
            emailUtil.confirmAccount(email, token);
            return ResponseEntity.ok(new ResponseObject(Validation.OK, "Please check email " + email, null));
        }
        if (optionalUser.get().getStatus().equals("INACTIVE")) {
            String token = jwtServiceImp.generateToken(optionalUser.get());
            emailUtil.confirmAccount(email, token);
            return ResponseEntity.ok(new ResponseObject(Validation.OK, "Please check email " + email, null));
        }
        return ResponseEntity.ok(new ResponseObject(Validation.FAIL, "Email " + email + " is exist !", null));
    }

    @Override
    public ResponseEntity<String> verifyAccount(String token) throws MessagingException {
        if (jwtServiceImp.isTokenExpired(token)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token is expired !!!");
        }
        Long userId = jwtServiceImp.extractUserId(token);
        Optional<User> optionalUser = userRepository.findById2(userId);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Found !!!");
        }
        User user = optionalUser.get();
        String password = PasswordGenerator.generateRandomPassword(8);
        user.setPassword(passwordEncoder.encode(password));
        user.setStatus("ACTIVE");
        userRepository.save(user);
        emailUtil.sendEmailWithInforNewAccount(user.getEmail(), user.getUser_name(), password);
        return ResponseEntity.ok("Account created successfully! Please check your email for login details.");
    }

    @Override
    public ResponseEntity<ResponseObject> searchUsers(String keyword) {
        User user = CommonUtils.getUserInformationLogin();
        if (user.getRole().equals(CommonConstant.ADMIN)) {
            return ResponseEntity.ok(new ResponseObject(Validation.OK, "ListUser found successfully !!!", userRepository.searchUsersForAdmin(keyword)));
        }
        return ResponseEntity.ok(new ResponseObject(Validation.OK, "ListUser found successfully !!!"
                , userRepository.searchUsersForManager(keyword, user.getCompany_id())));
    }

    public ResponseEntity<String> setPassword(String email, String newPassword) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found!");
        }
        User user = optionalUser.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return ResponseEntity.ok("Set new Password successfully !");
    }

    public ResponseEntity<ResponseObject> getUserProfileById(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        return optionalUser.map(user -> ResponseEntity.ok(new ResponseObject(Validation.OK, "User profile", user)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(Validation.FAIL, "userId" + userId + " is not exist", null)));
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user_id(user.getId())
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    @Override
    public ResponseEntity<ResponseObject> authenticated(UserDTO request) {
        Optional<User> optionalUser = userRepository.findByUserName(request.getUser_name());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("Username incorrect", "Username are incorrect !", ""));
        } else if (!passwordEncoder.matches(request.getPassword(), optionalUser.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("Password incorrect", "Password are incorrect !", ""));
        } else if (optionalUser.get().getStatus().equals("INACTIVE")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("Account inactive", "Inactive account !", ""));
        }
        User user = optionalUser.get();
        var jwt = jwtServiceImp.generateToken(user);
        var refreshToken = jwtServiceImp.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwt);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(String.valueOf(user.getId()), request.getPassword()));
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(Validation.OK, "Login successfully !", AuthenticationResponse.builder().accessToken(jwt).refresh_token(refreshToken).build()));
    }

    @Override
    public ResponseEntity<ResponseObject> logout(HttpServletRequest request, HttpServletResponse response) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final Long userId;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseObject(Validation.FAIL, "Authorization header is missing!", ""));
        }
        refreshToken = authHeader.substring(7);
        userId = jwtServiceImp.extractUserId(refreshToken);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseObject(Validation.FAIL, "Token does not exist !", ""));
        }
        var user = this.userRepository.findById(userId)
                .orElseThrow();
        if (!jwtServiceImp.isTokenValid(refreshToken, user)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseObject(Validation.FAIL, "Token invalid !", ""));
        }
        revokeAllUserTokens(user);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(Validation.OK, "Logout Successfully !", ""));
    }

    @Override
    public ResponseEntity<ResponseObject> extractUser() {
        String header = request.getHeader("Authorization");
        Map<String, Object> userInformation = extractUserInfoFromToken(header.substring(7));
        Optional<User> checkMailExist = userRepository.findByEmail(userInformation.get("email").toString().trim());
        if (checkMailExist.isPresent()) {
            User user = checkMailExist.get();
            String jwt = jwtServiceImp.generateToken(user);
            String refreshToken = jwtServiceImp.generateRefreshToken(user);
            revokeAllUserTokens(user);
            saveUserToken(user, jwt);
            return ResponseEntity.ok(new ResponseObject("OK", "Handle authorization code and state successfully!", AuthenticationResponse.builder().accessToken(jwt).refresh_token(refreshToken).build()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(Validation.FAIL, "Email does not exist!", ""));
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

    private String generateUsername(String email) {
        String usernameBase = email.split("@")[0];
        String newUsername = usernameBase;
        int suffix = 1;

        while (checkUsernameExists(newUsername)) {
            newUsername = usernameBase + suffix;
            suffix++;
        }
        return newUsername;
    }

    private boolean checkUsernameExists(String username) {
        return userRepository.findByUserNameForRegister(username).isPresent();
    }
}
