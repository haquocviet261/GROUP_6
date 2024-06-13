package com.petshop.services.interfaces;

import com.petshop.model.dto.request.FoodItemRequest;
import com.petshop.model.dto.response.ResponseObject;
import com.petshop.model.entity.FoodItem;
import org.springframework.http.ResponseEntity;

public interface FoodItemService {
    ResponseEntity<ResponseObject> getAllFoodItem();
    ResponseEntity<ResponseObject> addFoodItem(FoodItemRequest foodItemRequest);
}
