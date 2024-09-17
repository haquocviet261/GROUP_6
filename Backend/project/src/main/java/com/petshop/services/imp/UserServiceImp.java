package com.petshop.services.imp;

import com.petshop.common.constant.Role;
import com.petshop.common.utils.EmailUtils;
import com.petshop.common.utils.Validation;
import com.petshop.mapper.MapperImp.UserMapper;
import com.petshop.model.dto.request.ChangePasswordRequest;
import com.petshop.model.dto.request.EditDTO;
import com.petshop.model.dto.request.UserDto;
import com.petshop.model.dto.response.ResponseObject;
import com.petshop.model.entity.OnlineStatus;
import com.petshop.model.entity.User;
import com.petshop.repositories.UserRepository;
import com.petshop.services.interfaces.UserService;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service

public class UserServiceImp implements UserService {


    @Autowired
    JwtServiceImp jwtServiceImp;
    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userrepository;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EmailUtils emailUtil;
    @Autowired
    private OnlineStatusRepository onlineStatusRepository;



    public ResponseEntity<ResponseObject> changePassword(ChangePasswordRequest request, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ResponseObject(Validation.OK,"Wrong Password",null));

        }
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ResponseObject(Validation.OK,"Confirm password are not the same",null));

        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userrepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(Validation.OK,"Change Password successfully !",null));
    }
    public ResponseEntity<ResponseObject> getAllUsers() {
        List<User> users = userrepository.findAll();
        List<UserDto> userDtos = new ArrayList<>();
        for(User user:users){
            userDtos.add(userMapper.mapTo(user));
        }
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject(Validation.FAIL, "No users found", null));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(Validation.OK, "Retrieved users successfully",userDtos));
    }

    public ResponseEntity<String> forgotPassword(String email) throws MessagingException {

        User user =  userrepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Email is not exist"));
        String jwt = jwtServiceImp.generateToken(user,true);
        emailUtil.sendEmail(email,jwt);
        return ResponseEntity.ok("Please check you email to set password");
    }
    public ResponseEntity<String> verifyAccount(String email,String jwt){
         User user = userrepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Email not found !"));
         if (jwtServiceImp.isTokenValid(jwt,user)){
             return ResponseEntity.ok("Verify Account successfully !");
         }else {
             return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Email is Expired please try send Email again !");
         }
    }

    public String setPassword(String email,String newPassword){
        User user = userrepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Email not found this email !"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userrepository.save(user);
        return "Set new Password successfully !";
    }

    public ResponseEntity<ResponseObject> findById(Long userId) {
        return ResponseEntity.ok(new ResponseObject("OK","User profile",userrepository.findById(userId)));
    }

    @Override
    public void disconnect(Long user_id) {
        Optional<User> user = userrepository.findById(user_id);
        if (user.isPresent()){
            OnlineStatus onlineStatus = onlineStatusRepository.getOnlineStatusByUserID(user_id);
            onlineStatus.setStatus(com.petshop.common.constant.OnlineStatus.OFFLINE.getCode());
            onlineStatusRepository.save(onlineStatus);
        }
    }

    @Override
    public List<User> findAllUserOnline() {
        return userrepository.findAllUserOnline(com.petshop.common.constant.OnlineStatus.ONLINE.getCode());
    }



    public void saveOnlineStatusUser(Long user_id){
        Optional<User> user = userrepository.findById(user_id);
        if (user.isPresent()){
            OnlineStatus onlineStatus = onlineStatusRepository.getOnlineStatusByUserID(user_id);
            onlineStatus.setStatus(com.petshop.common.constant.OnlineStatus.ONLINE.getCode());
            onlineStatusRepository.save(onlineStatus);
        }
    }

    public ResponseEntity<String> editUser(EditDTO editDTO, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        user.setFirst_name(editDTO.getFirstname());
        user.setLast_name(editDTO.getLastname());
        user.setAddress(editDTO.getAddress());
        user.setDate_of_birth(editDTO.getDateofbirth());
        userrepository.save(user);
        return ResponseEntity.ok("Edit user profile successfully !");
    }
    @Override
    public List<UserStatusResponse> getListUserWithStatus() {
        List<Object[]> list = userrepository.getListUserWithStatus();
        List<UserStatusResponse> responses = new ArrayList<>();
        for (int i = 0; i <list.size() ; i++) {
            User user = (User) list.get(i)[0];
            Long status = (Long) list.get(i)[1];
            if (user.getRole() == Role.customer){
                responses.add(new UserStatusResponse(user.getUser_id(),user.getUsername(),user.getFirst_name(),user.getLast_name(),user.getImage_src(),status));

            }
        }
        return responses;
    }

    @Override
    public ResponseEntity<ResponseObject> findByUserName(String user_name) {
        Optional<User> user = userrepository.findByUserName(user_name);
        return user.isPresent() ? ResponseEntity.ok(new ResponseObject("OK","Find user successfully",user.get()))
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("Fail","Cannot find user with username:"+user_name,""));
    }
    @Override
    public ResponseEntity<ResponseObject> findByUserID(Long user_id) {
        Optional<User> user = userrepository.findById(user_id);
        return user.isPresent() ? ResponseEntity.ok(new ResponseObject("OK","Find user successfully",user.get()))
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("Fail","Cannot find user with username:"+user_id,""));
    }

    @Override
    public ResponseEntity<ResponseObject> getUserWithStatus(Long user_id) {
        UserStatusResponse userStatusResponse = userrepository.getUserWithStatus(user_id);
        return ResponseEntity.ok(new ResponseObject("OK","Usser wwith Status: ",userStatusResponse));
    }

}
