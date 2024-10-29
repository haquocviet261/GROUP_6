package com.iot.controller;

import com.iot.model.dto.request.FoodItemRequest;
import com.iot.model.dto.response.ResponseObject;
import com.iot.model.entity.FoodItem;
import com.iot.services.imp.FoodItemServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/food-item")
public class FoodItemController {
    @Autowired
    FoodItemServiceImp foodItemServiceImp;

    @GetMapping("/all")
    public ResponseEntity<ResponseObject> getAll() {
        return foodItemServiceImp.getAllFoodItem();
    }

    @GetMapping("/all/sort-name-asc")
    public ResponseEntity<ResponseObject> getAllFoodItemSortByNameAsc() {
        return foodItemServiceImp.getAllFoodItemSortByNameAsc();
    }

    @GetMapping("/all/sort-name-desc")
    public ResponseEntity<ResponseObject> getAllFoodItemSortByNameDesc() {
        return foodItemServiceImp.getAllFoodItemSortByNameDesc();
    }

    @GetMapping("/all/sort-expiration-asc")
    public ResponseEntity<ResponseObject> getAllFoodItemSortByExpirationDateAsc() {
        return foodItemServiceImp.getAllFoodItemSortByExpirationDateAsc();
    }

    @GetMapping("/all/sort-expiration-desc")
    public ResponseEntity<ResponseObject> getAllFoodItemSortByExpirationDateDesc() {
        return foodItemServiceImp.getAllFoodItemSortByExpirationDateDesc();
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseObject> addFoodItem(@RequestBody FoodItemRequest foodItemRequest) {
        return foodItemServiceImp.addFoodItem(foodItemRequest);
    }

    @PostMapping("/update/{foodItem_id}")
    public ResponseEntity<ResponseObject> updateFoodItem(@PathVariable Long foodItem_id, @RequestBody FoodItemRequest foodItemRequest) {
        return foodItemServiceImp.updateFoodItem(foodItem_id, foodItemRequest);
    }

    @GetMapping("/clear-data/{foodItem_id}")
    public ResponseEntity<String> clearDataFoodItem(@PathVariable Long foodItem_id){
        return foodItemServiceImp.clearDataFoodItem(foodItem_id);
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseObject> searchFoodItem(@RequestParam(name = "keyword") String keyword){
        return foodItemServiceImp.searchFoodItem(keyword);
    }
}
