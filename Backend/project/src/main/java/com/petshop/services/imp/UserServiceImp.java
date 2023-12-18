package com.petshop.services.imp;

import com.petshop.common.utils.EmailUtils;
import com.petshop.mapper.MapperImp.UserMapper;
import com.petshop.models.dto.request.ChangePasswordRequest;
import com.petshop.models.dto.request.EditDTO;
import com.petshop.models.dto.request.UserDto;
import com.petshop.models.dto.response.ResponseObject;
import com.petshop.models.entities.User;
import com.petshop.repositories.UserRepository;
import com.petshop.common.utils.Validation;
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

    public ResponseEntity<String> editUser(EditDTO editDTO,Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        user.setFirstName(editDTO.getFirstname());
        user.setLastName(editDTO.getLastname());
        user.setAddress(editDTO.getAddress());
        user.setDateOfBirth(editDTO.getDateofbirth());
        userrepository.save(user);
        return ResponseEntity.ok("Edit user profile successfully !");
    }

}
