package com.iot.services.interfaces;

import com.iot.model.dto.request.FoodItemRequest;
import com.iot.model.dto.response.ResponseObject;
import org.springframework.http.ResponseEntity;

public interface FoodItemService {
    ResponseEntity<ResponseObject> getAllFoodItem();

    ResponseEntity<ResponseObject> getAllFoodItemSortByNameAsc();

    ResponseEntity<ResponseObject> getAllFoodItemSortByNameDesc();

    ResponseEntity<ResponseObject> getAllFoodItemSortByExpirationDateAsc();

    ResponseEntity<ResponseObject> getAllFoodItemSortByExpirationDateDesc();

    ResponseEntity<ResponseObject> addFoodItem(FoodItemRequest foodItemRequest);

    ResponseEntity<ResponseObject> updateFoodItem(Long id, FoodItemRequest foodItemRequest);

    ResponseEntity<String> clearDataFoodItem(Long id);

    ResponseEntity<ResponseObject> searchFoodItem(String keyword);
}
