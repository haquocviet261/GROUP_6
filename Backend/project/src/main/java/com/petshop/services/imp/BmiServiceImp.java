package com.petshop.services.imp;

import com.petshop.common.constant.Gender;
import com.petshop.model.dto.request.BmiRequest;
import com.petshop.model.dto.response.BmiResponse;
import com.petshop.model.dto.response.ResponseObject;
import com.petshop.model.entity.Bmi;
import com.petshop.model.entity.User;
import com.petshop.repositories.BmiRepository;
import com.petshop.repositories.UserRepository;
import com.petshop.services.interfaces.BmiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BmiServiceImp implements BmiService {
    @Autowired
    private BmiRepository bmiRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public ResponseEntity<ResponseObject> addBmi(BmiRequest bmiRequest) {
        User user = userRepository.findById(bmiRequest.getUser_id()).get();
        Bmi bmi = new Bmi(null,bmiRequest.getWeight(),bmiRequest.getHeight(),bmiRequest.getAge(), Gender.valueOf(bmiRequest.getGender()),user);
        return ResponseEntity.ok(new ResponseObject("OK","Add BMI successfully",bmiRepository.save(bmi)));
    }

    @Override
    public ResponseEntity<ResponseObject> getBmiBeyUserID(Long user_id) {
        BmiResponse bmi =bmiRepository.getBmiByUserID(user_id);
        return ResponseEntity.ok(new ResponseObject("OK","Add BMI successfully",bmi));
    }
}
