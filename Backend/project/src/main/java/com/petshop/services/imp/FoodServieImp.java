package com.petshop.services.imp;

import com.petshop.model.dto.response.ResponseObject;
import com.petshop.repositories.FoodRepository;
import com.petshop.services.interfaces.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class FoodServieImp implements FoodService {
    @Autowired
    private FoodRepository foodRepository;
    @Override
    public ResponseEntity<ResponseObject> getAllFood() {
        return ResponseEntity.ok(new ResponseObject("OK","List Food",foodRepository.findAll()));
    }
}
