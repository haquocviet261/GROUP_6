package com.petshop.services.interfaces;

import com.petshop.models.dto.request.UserDto;
import com.petshop.models.dto.request.RegisterRequest;
import com.petshop.models.dto.response.ResponseObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface AuthenticationService {
    public ResponseEntity<ResponseObject> register(RegisterRequest request);
    public ResponseEntity<ResponseObject> authenticated(UserDto request);
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
