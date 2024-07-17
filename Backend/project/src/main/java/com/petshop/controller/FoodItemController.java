package com.petshop.controller;

import com.petshop.model.dto.request.FoodItemRequest;
import com.petshop.model.dto.response.ResponseObject;
import com.petshop.services.imp.FoodItemServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/food/item")
public class FoodItemController {
    @Autowired
    public FoodItemServiceImp foodItemServiceImp;
    @GetMapping("/all")
    public ResponseEntity<ResponseObject> getAllFood(){
        return foodItemServiceImp.getAllFoodItem();
    }
    @PostMapping("/add")
    public ResponseEntity<ResponseObject> addFoodItem(@RequestBody FoodItemRequest foodItemRequest){
        return foodItemServiceImp.addFoodItem(foodItemRequest);
    }
    @GetMapping("/find")
    public ResponseEntity<ResponseObject> getFoodItemByUserIDAndDeviceItemID(@RequestParam Long device_item_id){
        return foodItemServiceImp.getFoodItemByUserIDAndDeviceItemID(device_item_id);
    }
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseObject> deleteFoodItemByFoodItemId(@RequestParam Long food_item_id){
        return foodItemServiceImp.deleteFoodItemByFoodItemId(food_item_id);
    }
}
