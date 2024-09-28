package com.iot.services.imp;

import com.iot.common.utils.EmailUtils;
import com.iot.common.utils.Validation;
import com.iot.mapper.MapperImp.EditMapper;
import com.iot.mapper.MapperImp.UserMapper;
import com.iot.model.dto.request.ChangePasswordRequest;
import com.iot.model.dto.request.EditUserDTO;
import com.iot.model.dto.request.UserDTO;
import com.iot.model.dto.response.ResponseObject;
import com.iot.model.entity.User;
import com.iot.repositories.UserRepository;
import com.iot.services.interfaces.UserService;

import jakarta.mail.MessagingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {


    @Autowired
    JwtServiceImp jwtServiceImp;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userrepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private EditMapper editMapper;
    @Autowired
    private EmailUtils emailUtil;

    public ResponseEntity<ResponseObject> changePassword(ChangePasswordRequest request, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ResponseObject(Validation.OK, "Wrong Password", null));
        }
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ResponseObject(Validation.OK, "Confirm password are not the same", null));

        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userrepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(Validation.OK, "Change Password successfully !", null));
    }

    public ResponseEntity<ResponseObject> getAllUsers() {
        List<UserDTO> userDTOS = userrepository.getAllUser();
        if (userDTOS.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject(Validation.FAIL, "No users found", null));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(Validation.OK, "Retrieved users successfully", userDTOS));
    }

    public ResponseEntity<ResponseObject> deleteUser(Long id) {
        User user = userrepository.findById(id).orElseThrow(() -> new RuntimeException("Id" + id + "is not exist"));
        user.setDeleted_at(new Date());
        user.setStatus("INACTIVE");
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(Validation.OK, "Deleted User" + id + " successfully!", userrepository.save(user)));
    }

    public ResponseEntity<ResponseObject> updateUser(EditUserDTO editUserDTO, Long id){
        User user = userrepository.findById(id).orElseThrow(() -> new RuntimeException("Id" + id + "is not exist"));
        BeanUtils.copyProperties(editUserDTO,user);
        user.setUpdated_at(new Date());
//        user.setUpdated_by("");
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(Validation.OK, "Updated User" + user.getId() + " successfully!", userrepository.save(user)));
    }

    public ResponseEntity<ResponseObject> addUser(UserDTO userDTO) {
        User user = userMapper.mapFrom(userDTO);
        user.setCreated_at(new Date());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(Validation.OK, "Added User" + user.getId() + " successfully!", userrepository.save(user)));
    }

    public ResponseEntity<String> forgotPassword(String email) throws MessagingException {

        User user = userrepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Email is not exist"));
        String jwt = jwtServiceImp.generateToken(user, true);
        emailUtil.sendEmail(email, jwt);
        return ResponseEntity.ok("Please check you email to set password");
    }

    public ResponseEntity<String> verifyAccount(String email, String jwt) {
        User user = userrepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Email not found !"));
        if (jwtServiceImp.isTokenValid(jwt, user)) {
            return ResponseEntity.ok("Verify Account successfully !");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Email is Expired please try send Email again !");
        }
    }

    public String setPassword(String email, String newPassword) {
        User user = userrepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Email not found this email !"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userrepository.save(user);
        return "Set new Password successfully !";
    }

    public ResponseEntity<ResponseObject> findById(Long userId) {
        return ResponseEntity.ok(new ResponseObject("OK", "User profile", userrepository.findById(userId)));
    }

    public ResponseEntity<String> editUser(EditUserDTO editUserDTO, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        user.setFirstname(editUserDTO.getFirstname());
        user.setLastname(editUserDTO.getLastname());
        user.setAddress(editUserDTO.getAddress());
        user.setDate_of_birth(editUserDTO.getDate_of_birth());
        userrepository.save(user);
        return ResponseEntity.ok("Edit user profile successfully !");
    }

    @Override
    public ResponseEntity<ResponseObject> findByUserID(Long user_id) {
        Optional<User> user = userrepository.findById(user_id);
        return user.isPresent() ? ResponseEntity.ok(new ResponseObject("OK", "Find user successfully", user.get()))
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("Fail", "Cannot find user with username:" + user_id, ""));
    }

}
