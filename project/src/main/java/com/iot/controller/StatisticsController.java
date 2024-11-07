package com.iot.controller;

import com.iot.model.dto.response.ResponseObject;
import com.iot.services.interfaces.FoodItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/statistic")
public class StatisticsController {
    @Autowired
    private FoodItemService foodItemService;
    @RequestMapping(value = "/food-expried", method = RequestMethod.GET)
    public ResponseEntity<ResponseObject> getFoodExpired(@RequestParam("company_id") Long company_id) {
        return foodItemService.getExpiredFoodItems(company_id);
    }
}
