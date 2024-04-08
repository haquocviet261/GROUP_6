package com.petshop.services.interfaces;

import com.petshop.models.dto.request.ChangePasswordRequest;
import com.petshop.models.dto.response.ResponseObject;
import com.petshop.models.dto.response.UserStatusResponse;
import com.petshop.models.entities.User;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;

public interface UserService {

    public ResponseEntity<ResponseObject> changePassword(ChangePasswordRequest request, Principal connectedUser);
    public ResponseEntity<ResponseObject> getAllUsers();
    public ResponseEntity<ResponseObject> findById(Long user_id);
    public void disconnect(Long user_id);
    public List<User> findAllUserOnline();
    public List<UserStatusResponse> getListUserWithStatus();
}
