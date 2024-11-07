package com.iot.controller;

import com.iot.model.dto.response.ResponseObject;
import com.iot.services.imp.FoodCategoryServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/food-category")
public class FoodCategoryController {
    @Autowired
    private FoodCategoryServiceImp foodCategoryServiceImp;

    @GetMapping("/all")
    public ResponseEntity<ResponseObject> getAll(){
        return foodCategoryServiceImp.getAllFoodCategory();
    }
}
