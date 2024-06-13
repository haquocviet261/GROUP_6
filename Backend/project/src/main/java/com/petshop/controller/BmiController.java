package com.petshop.controller;

import com.petshop.model.dto.request.BmiRequest;
import com.petshop.model.dto.response.ResponseObject;
import com.petshop.model.entity.Bmi;
import com.petshop.services.imp.BmiServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/bmi")
public class BmiController {
    @Autowired
    private BmiServiceImp bmiServiceImp;
    @PostMapping("/add")
    public ResponseEntity<ResponseObject> addBmi(@RequestBody BmiRequest bmi){
        return bmiServiceImp.addBmi(bmi);
    }
    @GetMapping("/get")
    public ResponseEntity<ResponseObject> getBmiByUserID(@RequestParam Long user_id){
        return bmiServiceImp.getBmiBeyUserID(user_id);
    }
}
