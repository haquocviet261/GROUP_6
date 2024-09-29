package com.iot.services.interfaces;

import com.iot.model.dto.request.ChangePasswordRequest;
import com.iot.model.dto.request.RegisterRequest;
import com.iot.model.dto.request.UserDTO;
import com.iot.model.dto.response.ResponseObject;
import com.iot.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

public interface UserService {
    ResponseEntity<ResponseObject> changePassword(ChangePasswordRequest request, Principal connectedUser);

    ResponseEntity<ResponseObject> getAllUsers();

    ResponseEntity<ResponseObject> findByUserId(Long user_id);
    ResponseEntity<ResponseObject> addUser(RegisterRequest request);
    ResponseEntity<ResponseObject> authenticated(UserDTO request);
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
