package com.iot.services.interfaces;

import com.iot.model.dto.request.*;
import com.iot.model.dto.response.ResponseObject;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface UserService {
    ResponseEntity<ResponseObject> changePassword(ChangePasswordRequest request);

    ResponseEntity<ResponseObject> getAllUsers();

    ResponseEntity<ResponseObject> editUser(EditUserDTO editUserDTO);

    ResponseEntity<ResponseObject> authenticated(UserDTO request);

    ResponseEntity<ResponseObject> logout(HttpServletRequest request, HttpServletResponse response) throws IOException;

    ResponseEntity<ResponseObject> extractUser();

    ResponseEntity<ResponseObject> resetPassword(ForgotPasswordRequest request) throws MessagingException;

    ResponseEntity<ResponseObject> register(RegisterRequest request) throws MessagingException;

    ResponseEntity<String> verifyAccount(String email) throws MessagingException;

    ResponseEntity<ResponseObject> searchUsers(String keyword);
}
