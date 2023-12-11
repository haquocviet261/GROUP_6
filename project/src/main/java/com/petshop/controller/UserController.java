package com.petshop.controller;



import com.petshop.models.dto.request.ChangePasswordRequest;

import com.petshop.models.dto.response.ResponseObject;
import com.petshop.services.imp.AuthenticationServiceImp;
import com.petshop.services.imp.UserServiceImp;
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
    @PutMapping("/forgot-password")
    public ResponseEntity<?> forgotpassword(@RequestParam String email){
        return userServiceImp.forgotPassword(email);
    }
    @PutMapping("/set-password")
    public ResponseEntity<?> setPassword(@RequestParam String email,@RequestHeader String newPassword){
        return ResponseEntity.ok(userServiceImp.setPassword(email,newPassword));
    }
}
