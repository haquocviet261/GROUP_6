package com.iot.controller;

import com.iot.model.dto.request.ChangePasswordRequest;
import com.iot.model.dto.request.RegisterRequest;
import com.iot.model.dto.response.ResponseObject;
import com.iot.model.dto.request.EditUserDTO;
import com.iot.model.dto.request.UserDTO;
import com.iot.services.imp.UserServiceImp;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("api/user")
public class UserController {
    @Autowired
    private UserServiceImp userServiceImp;

    @GetMapping("/logout")
    public ResponseEntity<ResponseObject> logout(HttpServletRequest request, HttpServletResponse response){
        return userServiceImp.logout(request, response);
    }

    @PatchMapping("/change-password")
    public ResponseEntity<ResponseObject> changePassword
            (@RequestBody ChangePasswordRequest request) {
        return userServiceImp.changePassword(request);
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return userServiceImp.getAllUsers();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody String email) throws MessagingException {
        return userServiceImp.resetPassword(email);
    }

    @PutMapping("/set-password")
    public ResponseEntity<String> setPassword(@RequestBody String email, @RequestBody String newPassword) {
        return userServiceImp.setPassword(email, newPassword);
    }

    @GetMapping("/profile")
    public ResponseEntity<ResponseObject> showProfile(@RequestParam Long user_id) {
        return userServiceImp.getUserProfileById(user_id);
    }

    @PostMapping("/register-admin")
    public ResponseEntity<String> registerAdmin(@RequestBody RegisterRequest request) throws MessagingException {
        return userServiceImp.registerAdmin(request);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) throws MessagingException {
        return userServiceImp.register(request);
    }

    @GetMapping("/verify-account")
    public ResponseEntity<String> verifyAccount(@RequestParam("token") String token) throws MessagingException {
        return userServiceImp.verifyAccount(token);
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseObject> getAll() {
        return userServiceImp.getAllUsers();
    }

    @PostMapping("/delete/{user_id}")
    public ResponseEntity<ResponseObject> deleteUser(@PathVariable Long user_id) {
        return userServiceImp.deleteUser(user_id);
    }

    @PostMapping("/edit-user")
    public ResponseEntity<ResponseObject> editUser(@RequestBody EditUserDTO request) {
        return userServiceImp.editUser(request);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<ResponseObject> authenticated(@RequestBody UserDTO request) {
        return userServiceImp.authenticated(request);
    }

    @GetMapping("/oauth2/google")
    public ResponseEntity<ResponseObject> getToken() {
        return userServiceImp.extractUser();
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseObject> searchUsers(@RequestParam(name = "keyword") String keyword){
        return userServiceImp.searchUsers(keyword);
    }
}
