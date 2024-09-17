package com.petshop.services.interfaces;

import com.petshop.model.dto.request.ChangePasswordRequest;
import com.petshop.model.dto.response.ResponseObject;
import com.petshop.model.entity.User;
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
    public ResponseEntity<ResponseObject> findByUserName(String user_name);
    ResponseEntity<ResponseObject> getUserWithStatus(Long user_id);
    ResponseEntity<ResponseObject> findByUserID(Long user_id);
}
