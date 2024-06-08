package com.petshop.services.interfaces;


import com.petshop.model.dto.request.RegisterRequest;
import com.petshop.model.dto.request.UserDto;
import com.petshop.model.dto.response.ResponseObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface AuthenticationService {
     ResponseEntity<ResponseObject> register(RegisterRequest request);
     ResponseEntity<ResponseObject> authenticated(UserDto request);
     void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
