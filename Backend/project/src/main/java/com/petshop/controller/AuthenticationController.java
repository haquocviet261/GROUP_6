package com.petshop.controller;

import com.petshop.models.dto.request.UserDto;
import com.petshop.models.dto.request.RegisterRequest;
import com.petshop.models.dto.response.ResponseObject;
import com.petshop.services.imp.AuthenticationServiceImp;
import com.petshop.services.imp.UserServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;


@RestController
@RequestMapping("api/auth")
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
