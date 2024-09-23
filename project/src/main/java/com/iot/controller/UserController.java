package com.iot.controller;



import com.iot.model.dto.request.ChangePasswordRequest;
import com.iot.model.dto.response.ResponseObject;

import com.iot.model.dto.request.EditDTO;
import com.iot.model.dto.request.RegisterRequest;
import com.iot.model.dto.request.UserDto;
import com.iot.repositories.UserRepository;
import com.iot.services.imp.AuthenticationServiceImp;
import com.iot.services.imp.UserServiceImp;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("api/user")
public class UserController {
    @Autowired
    private UserServiceImp userServiceImp;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private AuthenticationServiceImp authenticationServiceImp;
    @GetMapping("/logout")
    public  String logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authenticationServiceImp.refreshToken(request, response);
        return "Logout Successfully !";
    }

    @PatchMapping("/change-password")
    public ResponseEntity<ResponseObject> changePassword
            (@RequestBody ChangePasswordRequest request, Principal connectedUser) {
        return userServiceImp.changePassword(request, connectedUser);
    }
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return userServiceImp.getAllUsers();
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotpassword(@RequestBody String email) throws MessagingException {
        return userServiceImp.forgotPassword(email);
    }
    @GetMapping("/verify-account")
    public ResponseEntity<String> verifyAccount(@RequestParam(name = "email") String email,@RequestParam(name = "jwt") String jwt){

        return userServiceImp.verifyAccount(email,jwt);
    }
    @PutMapping("/set-password")
    public ResponseEntity<String> setPassword(@RequestBody String email,@RequestBody String newPassword){
        return ResponseEntity.ok(userServiceImp.setPassword(email,newPassword));
    }
    @GetMapping("/profile")
    public ResponseEntity<ResponseObject> showProfile(@RequestParam Long user_id){
        return userServiceImp.findById(user_id);
    }
    @GetMapping("/edit_user")
    public ResponseEntity<?> editProfile(@RequestBody EditDTO editDTO, Principal connectedUser){
        return ResponseEntity.ok(userServiceImp.editUser(editDTO, connectedUser));
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseObject> register(@RequestBody RegisterRequest request){
        return authenticationServiceImp.register(request);
    }
    @GetMapping("/all")
    public ResponseEntity<ResponseObject> getAll(){
       return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK","Get List User successfuly !",userRepository.findAll()));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<ResponseObject> authenticated(@RequestBody UserDto request){
        return authenticationServiceImp.authenticated(request);
    }
    @GetMapping("/oauth2/google")
    public ResponseEntity<ResponseObject> getToken() {
        return authenticationServiceImp.extracUser();
    }
    @GetMapping("/{user_id}")
    public ResponseEntity<ResponseObject> findUserByUserID(@PathVariable Long user_id){
        return userServiceImp.findByUserID(user_id);
    }
}
