package com.iot.services.imp;

import com.iot.model.dto.response.ResponseObject;
import com.iot.repositories.FoodRepository;
import com.iot.services.interfaces.FoodService;
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
