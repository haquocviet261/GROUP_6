package com.petshop.controller;



import com.petshop.models.dto.request.ChangePasswordRequest;

import com.petshop.models.dto.request.EditDTO;
import com.petshop.models.dto.response.ResponseObject;
import com.petshop.services.imp.AuthenticationServiceImp;
import com.petshop.services.imp.UserServiceImp;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("api/v1/user")
public class UserController {
    @Autowired
    private UserServiceImp userServiceImp;
    @Autowired
    private AuthenticationServiceImp authenticationServiceImp;
    @GetMapping("/logout")
    public  String logout
            (HttpServletRequest request, HttpServletResponse response) throws IOException {
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
    @GetMapping("/forgot-password")
    public ResponseEntity<?> forgotpassword(@RequestParam String email) throws MessagingException {
        return userServiceImp.forgotPassword(email);
    }
    @GetMapping("/verify-account")
    public ResponseEntity<?> verifyAccount(@RequestParam(name = "email") String email,@RequestParam(name = "jwt") String jwt){

        return ResponseEntity.ok(userServiceImp.verifyAccount(email,jwt));
    }
    @PutMapping("/set-password")
    public ResponseEntity<?> setPassword(@RequestParam String email,@RequestHeader String newPassword){
        return ResponseEntity.ok(userServiceImp.setPassword(email,newPassword));
    }
    @GetMapping("/profile")
    public ResponseEntity<?> showProfile(@RequestParam Long user_id){
        return ResponseEntity.ok(userServiceImp.findById(user_id));
    }
    @GetMapping("/edit_user")
    public ResponseEntity<?> editProfile(@RequestBody EditDTO editDTO,Principal connectedUser){
        return ResponseEntity.ok(userServiceImp.editUser(editDTO, connectedUser));
    }

}
