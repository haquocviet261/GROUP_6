package com.iot.services.interfaces;

import com.iot.model.dto.request.ChangePasswordRequest;
import com.iot.model.dto.request.RegisterRequest;
import com.iot.model.dto.request.UserDTO;
import com.iot.model.dto.response.ResponseObject;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface UserService {
    ResponseEntity<ResponseObject> changePassword(ChangePasswordRequest request);

    ResponseEntity<ResponseObject> getAllUsers();

    ResponseEntity<ResponseObject> findByUserId(Long user_id);

    ResponseEntity<ResponseObject> addUser(RegisterRequest request);

    ResponseEntity<ResponseObject> authenticated(UserDTO request);

    ResponseEntity<ResponseObject> logout(HttpServletRequest request, HttpServletResponse response) throws IOException;

    ResponseEntity<ResponseObject> extractUser();

    ResponseEntity<String> resetPassword(String email) throws MessagingException;

    ResponseEntity<String> register(String email) throws MessagingException;

    ResponseEntity<String> createAccount(String email) throws MessagingException;
}
