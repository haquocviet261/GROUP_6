package com.iot.services.interfaces;

import com.iot.model.dto.request.ChangePasswordRequest;
import com.iot.model.dto.response.ResponseObject;
import com.iot.model.entity.User;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;

public interface UserService {

     ResponseEntity<ResponseObject> changePassword(ChangePasswordRequest request, Principal connectedUser);
     ResponseEntity<ResponseObject> getAllUsers();
     ResponseEntity<ResponseObject> findById(Long user_id);
    ResponseEntity<ResponseObject> findByUserID(Long user_id);
}
