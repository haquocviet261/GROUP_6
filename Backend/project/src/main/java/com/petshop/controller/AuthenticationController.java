package com.petshop.controller;

import com.petshop.models.dto.request.UserDto;
import com.petshop.models.dto.request.RegisterRequest;
import com.petshop.models.dto.response.ResponseObject;
import com.petshop.services.imp.AuthenticationServiceImp;
import com.petshop.services.imp.UserServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping(value = "api/auth",method = RequestMethod.POST)

public class AuthenticationController {
    @Autowired
    private UserServiceImp userServiceImp;
    @Autowired
    private AuthenticationServiceImp authenticationServiceImp;
    @PostMapping("/register")
    public ResponseEntity<ResponseObject> register(@RequestBody RegisterRequest request){
        return authenticationServiceImp.register(request);
    }

    @PostMapping("/authenticate" )
    public ResponseEntity<ResponseObject> authenticated(@RequestBody UserDto request){
        return authenticationServiceImp.authenticated(request);
    }

}
