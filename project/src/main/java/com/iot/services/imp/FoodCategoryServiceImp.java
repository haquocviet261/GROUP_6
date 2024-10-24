package com.iot.services.imp;

import com.iot.common.utils.Validation;
import com.iot.model.dto.response.ResponseObject;
import com.iot.repositories.FoodCategoryRepository;
import com.iot.services.interfaces.FoodCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class FoodCategoryServiceImp implements FoodCategoryService {

    @Autowired
    FoodCategoryRepository foodCategoryRepository;

    @Override
    public ResponseEntity<ResponseObject> getAllFoodCategory() {
        return ResponseEntity.ok( new ResponseObject(Validation.OK, "Successfully !!!", foodCategoryRepository.getAllFoodCategory()));
    }
}
