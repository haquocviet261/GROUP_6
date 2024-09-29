package com.iot.services.imp;

import com.iot.common.utils.EmailUtils;
import com.iot.common.utils.PasswordGenerator;
import com.iot.common.utils.Validation;
import com.iot.mapper.MapperImp.EditMapper;
import com.iot.mapper.MapperImp.UserMapper;
import com.iot.model.dto.request.ChangePasswordRequest;
import com.iot.model.dto.request.EditUserDTO;
import com.iot.model.dto.request.RegisterRequest;
import com.iot.model.dto.request.UserDTO;
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
import org.modelmapper.ModelMapper;
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

import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  JwtServiceImp jwtServiceImp;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtDecoder jwtDecoder;
    @Autowired
    HttpServletRequest request;
    @Autowired
    private UserRepository userrepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private EditMapper editMapper;
    @Autowired
    private EmailUtils emailUtil;

    public ResponseEntity<ResponseObject> changePassword(ChangePasswordRequest request, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ResponseObject(Validation.OK, "Wrong Password", null));
        }
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ResponseObject(Validation.OK, "Confirm password are not the same", null));

        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userrepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(Validation.OK, "Change Password successfully !", null));
    }

    public ResponseEntity<ResponseObject> getAllUsers() {
        List<UserDTO> userDTOS = userrepository.getAllUser();
        if (userDTOS.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject(Validation.FAIL, "No users found", null));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(Validation.OK, "Retrieved users successfully", userDTOS));
    }

    public ResponseEntity<ResponseObject> deleteUser(Long id) {
        User user = userrepository.findById(id).orElseThrow(() -> new RuntimeException("Id" + id + "is not exist"));
        user.setDeleted_at(new Date());
        user.setStatus("INACTIVE");
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(Validation.OK, "Deleted User" + id + " successfully!", userrepository.save(user)));
    }

    public ResponseEntity<ResponseObject> updateUser(EditUserDTO editUserDTO, Long id){
        User user = userrepository.findById(id).orElseThrow(() -> new RuntimeException("Id" + id + "is not exist"));
        BeanUtils.copyProperties(editUserDTO,user);
        user.setUpdated_at(new Date());
//        user.setUpdated_by("");
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(Validation.OK, "Updated User" + user.getId() + " successfully!", userrepository.save(user)));
    }

    public ResponseEntity<ResponseObject> addUser(UserDTO userDTO) {
        User user = userMapper.mapFrom(userDTO);
        user.setCreated_at(new Date());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(Validation.OK, "Added User" + user.getId() + " successfully!", userrepository.save(user)));
    }

    public ResponseEntity<String> forgotPassword(String email) throws MessagingException {

        User user = userrepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Email is not exist"));
        String jwt = jwtServiceImp.generateToken(user, true);
        emailUtil.sendEmail(email, jwt);
        return ResponseEntity.ok("Please check you email to set password");
    }

    public ResponseEntity<String> verifyAccount(String email, String jwt) {
        User user = userrepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Email not found !"));
        if (jwtServiceImp.isTokenValid(jwt, user)) {
            return ResponseEntity.ok("Verify Account successfully !");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Email is Expired please try send Email again !");
        }
    }

    public String setPassword(String email, String newPassword) {
        User user = userrepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Email not found this email !"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userrepository.save(user);
        return "Set new Password successfully !";
    }

    public ResponseEntity<ResponseObject> findById(Long userId) {
        return ResponseEntity.ok(new ResponseObject("OK", "User profile", userrepository.findById(userId)));
    }

    public ResponseEntity<String> editUser(EditUserDTO editUserDTO, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        user.setFirstname(editUserDTO.getFirstname());
        user.setLastname(editUserDTO.getLastname());
        user.setAddress(editUserDTO.getAddress());
        user.setDate_of_birth(editUserDTO.getDate_of_birth());
        userrepository.save(user);
        return ResponseEntity.ok("Edit user profile successfully !");
    }

    @Override
    public ResponseEntity<ResponseObject> findByUserId(Long user_id) {
        Optional<User> user = userrepository.findById(user_id);
        return user.isPresent() ? ResponseEntity.ok(new ResponseObject("OK", "Find user successfully", user.get()))
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("Fail", "Cannot find user with username:" + user_id, ""));
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
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    @Override
    public ResponseEntity<ResponseObject> addUser(RegisterRequest request) {
        List<User> list = userRepository.findAll();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUsername().equals(request.getUsername())) {
                return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObject(Validation.FAIL, "User Name are exist !", ""));
            } else {
                if (list.get(i).getEmail().equals(request.getEmail())) {
                    return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObject(Validation.FAIL, "Email are exist !", ""));
                } else if (request.getPhonenumber().equals(list.get(i).getPhone_number())) {
                    return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObject(Validation.FAIL, "Phone number are exist !", ""));
                }
            }
        }
        var user = User.builder().user_name(request.getUsername()).firstname(request.getFirstname())
                .lastname(request.getLastname()).email(request.getEmail()).address(request.getAddress())
                .phone_number(request.getPhonenumber()).password(passwordEncoder.encode(request.getPassword())).role("USER").status("ACTIVE")
                .date_of_birth(request.getDateofbirth())
                .build();
        var savedUser = userRepository.save(user);
        var jwtToken = jwtServiceImp.generateToken(user);
        var refreshToken = jwtServiceImp.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(Validation.OK, "Register successfully !", AuthenticationResponse.builder().accessToken(jwtToken).refresh_token(refreshToken).build()));
    }

    @Override
    public ResponseEntity<ResponseObject> authenticated(UserDTO request) {

        var user = userRepository.findByUserName(request.getUser_name()).orElseThrow();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(Validation.FAIL, "User Name are incorrect !", ""));
        } else if (user.getPassword().equals(request.getPassword())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(Validation.FAIL, "Password are incorrect !", ""));
        }
        var jwt = jwtServiceImp.generateToken(user);
        var refreshToken = jwtServiceImp.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwt);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(String.valueOf(user.getId()), request.getPassword()));
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(Validation.OK, "Login successfully !", AuthenticationResponse.builder().accessToken(jwt).refresh_token(refreshToken).build()));
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userName;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
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

    public ResponseEntity<ResponseObject> extracUser() {
        User user;
        String jwt;
        String refreshToken;
        String header = request.getHeader("Authorization");
        Map<String, Object> userInfor = extractUserInfoFromToken(header.substring(7));
        Optional<User> checkMailExist = userRepository.findByEmail(userInfor.get("email").toString().trim());
        if (checkMailExist.isPresent()) {
            user = checkMailExist.get();
            jwt = jwtServiceImp.generateToken(user);
            refreshToken = jwtServiceImp.generateRefreshToken(user);
            revokeAllUserTokens(user);
            saveUserToken(user, jwt);
            return ResponseEntity.ok(new ResponseObject("OK", "Handle authorization code and state successfully!", AuthenticationResponse.builder().accessToken(jwt).refresh_token(refreshToken).build()));
        } else {
            String email = userInfor.get("email").toString().trim();
            user = User.builder().email(userInfor.get("email").toString().trim())
                    .user_name(userInfor.get("email").toString().trim().split("@")[0])
                    .role("USER")
                    .status("ACTIVE")
                    .email(email)
                    .password(passwordEncoder.encode(PasswordGenerator.generateRandomPassword(8)))
                    .firstname(userInfor.get("family_name").toString().trim())
                    .lastname(userInfor.get("given_name").toString().trim())
                    .images_src(userInfor.get("picture").toString())
                    .build();
            userRepository.save(user);
            jwt = jwtServiceImp.generateToken(user);
            refreshToken = jwtServiceImp.generateRefreshToken(user);
            revokeAllUserTokens(user);
            saveUserToken(user, jwt);
        }

        return ResponseEntity.ok(new ResponseObject("OK", "Handle authorization code and state successfully!", AuthenticationResponse.builder().accessToken(jwt).refresh_token(refreshToken).build()));
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
