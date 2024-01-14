package com.petshop.controller;

import com.petshop.models.dto.response.ResponseObject;
import com.petshop.services.imp.AuthenticationServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/login/oauth2/code")
public class Oauth2Controller {
    @Autowired
    AuthenticationServiceImp authenticationServiceImp;
    @GetMapping("/google")
    public ResponseEntity<ResponseObject> handleGoogleCallback(@RequestParam("code") String authorizationCode) {
        return authenticationServiceImp.handleGoogleCallback(authorizationCode);
    }
}
