package com.iot.controller;

import com.iot.model.dto.response.ResponseObject;
import com.iot.services.imp.FoodServieImp;
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
