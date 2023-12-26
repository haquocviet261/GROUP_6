package com.petshop.services.interfaces;

import com.petshop.models.dto.request.ChangePasswordRequest;
import com.petshop.models.dto.response.ResponseObject;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

public interface UserService {

    public ResponseEntity<ResponseObject> changePassword(ChangePasswordRequest request, Principal connectedUser);
    public ResponseEntity<ResponseObject> getAllUsers();
    public ResponseEntity<ResponseObject> findById(Long user_id);
}
