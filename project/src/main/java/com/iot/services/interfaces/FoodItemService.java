package com.iot.services.interfaces;

import com.iot.model.dto.request.FoodItemRequest;
import com.iot.model.dto.response.ResponseObject;
import org.springframework.http.ResponseEntity;

public interface FoodItemService {
    ResponseEntity<ResponseObject> getAllFoodItem();
    ResponseEntity<ResponseObject> addFoodItem(FoodItemRequest foodItemRequest);
}
