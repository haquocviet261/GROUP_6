package com.iot.services.interfaces;

import com.iot.model.dto.request.FoodItemRequest;
import com.iot.model.dto.response.ResponseObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

public interface FoodItemService {
    ResponseEntity<ResponseObject> getAllFoodItem();
    ResponseEntity<ResponseObject> getExpiredFoodItems(Long company_id);
    ResponseEntity<ResponseObject> addFoodItem(FoodItemRequest foodItemRequest);
}
