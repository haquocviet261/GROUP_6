package com.petshop.controller;



import com.petshop.model.dto.request.ChangePasswordRequest;
import com.petshop.model.dto.response.ResponseObject;

import com.petshop.model.dto.request.EditDTO;
import com.petshop.model.dto.request.RegisterRequest;
import com.petshop.model.dto.request.UserDto;
import com.petshop.model.dto.response.UserStatusResponse;
import com.petshop.model.entity.User;
import com.petshop.services.imp.AuthenticationServiceImp;
import com.petshop.services.imp.UserServiceImp;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
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
    public ResponseEntity<?> showProfile(@RequestParam Long user_id){
        return ResponseEntity.ok(userServiceImp.findById(user_id));
    }
    @GetMapping("/edit_user")
    public ResponseEntity<?> editProfile(@RequestBody EditDTO editDTO, Principal connectedUser){
        return ResponseEntity.ok(userServiceImp.editUser(editDTO, connectedUser));
    }
    @MessageMapping("/user.add_user")
    @SendTo("/user/public")
    public UserStatusResponse addUser(@Payload Long sender_id){
        userServiceImp.saveOnlineStatusUser(sender_id);
        ResponseEntity<ResponseObject> user = userServiceImp.getUserWithStatus(sender_id);
        return (UserStatusResponse) Objects.requireNonNull(user.getBody()).getData();
    }
    @MessageMapping("/user.disconnect")
    @SendTo("/user/public")
    public UserStatusResponse disconnect(@Payload Long sender_id){
        userServiceImp.disconnect(sender_id);
        ResponseEntity<ResponseObject> user = userServiceImp.getUserWithStatus(sender_id);
        return (UserStatusResponse) Objects.requireNonNull(user.getBody()).getData();
    }
    @GetMapping("/online_user")
    public ResponseEntity<List<UserStatusResponse>> findOnlineUser(){
        return ResponseEntity.ok(userServiceImp.getListUserWithStatus());
    }
    @PostMapping("/register")
    public ResponseEntity<ResponseObject> register(@RequestBody RegisterRequest request){
        return authenticationServiceImp.register(request);
    }

    @PostMapping("/authenticate" )
    public ResponseEntity<ResponseObject> authenticated(@RequestBody UserDto request){
        return authenticationServiceImp.authenticated(request);
    }
    @GetMapping("/oauth2/google")
    public ResponseEntity<ResponseObject> getToken() {
        return authenticationServiceImp.extracUser();
    }
    @GetMapping("/find")
    public ResponseEntity<ResponseObject> findUserByUserName(@RequestParam String user_name){
        return userServiceImp.findByUserName(user_name);
    }
    @GetMapping("/{user_id}")
    public ResponseEntity<ResponseObject> findUserByUserID(@PathVariable Long user_id){
        return userServiceImp.findByUserID(user_id);
    }
}
