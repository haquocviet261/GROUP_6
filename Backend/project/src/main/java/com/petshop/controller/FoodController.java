package com.petshop.controller;

import com.petshop.model.dto.response.ResponseObject;
import com.petshop.services.imp.FoodServieImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/food")
public class FoodController {
    @Autowired
    private FoodServieImp foodServieImp;

    @GetMapping("/all")
    public ResponseEntity<ResponseObject> getAllFood(){
        return foodServieImp.getAllFood();
    }
}
