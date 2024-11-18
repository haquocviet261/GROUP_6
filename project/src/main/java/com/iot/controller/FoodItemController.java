package com.iot.controller;

import com.iot.model.dto.request.FoodItemRequest;
import com.iot.model.dto.response.ResponseObject;
import com.iot.services.imp.FoodItemServiceImp;
import com.iot.services.interfaces.FoodItemService;
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

    @PostMapping("/add")
    public ResponseEntity<ResponseObject> addFoodItem(@RequestBody FoodItemRequest foodItemRequest) {
        return foodItemServiceImp.addFoodItem(foodItemRequest);
    }

    @PostMapping("/update/{foodItem_id}")
    public ResponseEntity<ResponseObject> updateFoodItem(@PathVariable Long foodItem_id, @RequestBody FoodItemRequest foodItemRequest) {
        return foodItemServiceImp.updateFoodItem(foodItem_id, foodItemRequest);
    }

    @GetMapping("/clear-data/{foodItem_id}")
    public ResponseEntity<ResponseObject> clearDataFoodItem(@PathVariable Long foodItem_id){
        return foodItemServiceImp.clearDataFoodItem(foodItem_id);
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseObject> searchFoodItem(@RequestParam(name = "keyword") String keyword){
        return foodItemServiceImp.searchFoodItem(keyword);
    }

    @GetMapping("/category/{category_id}")
    public ResponseEntity<ResponseObject> getFoodItemByCategory(@PathVariable(name = "category_id") Integer category_id) {
        return foodItemServiceImp.getFoodItemByCategory(category_id);
    }

    @GetMapping("/id/{foodItemId}")
    public ResponseEntity<ResponseObject> getFoodItemById(@PathVariable(name = "foodItemId") Integer foodItemId) {
        return foodItemServiceImp.getFoodItemById(foodItemId);
    }

    @Autowired
    private FoodItemService foodItemService;
    @RequestMapping(value = "/food-expried", method = RequestMethod.GET)
    public ResponseEntity<ResponseObject> getFoodExpired(@RequestParam("company_id") Long company_id) {
        return foodItemService.getExpiredFoodItems(company_id);
    }
    @RequestMapping(value = "/get-food-by-company-id", method = RequestMethod.GET)
    public ResponseEntity<ResponseObject> getFoodItemByCompanyId(Integer company_id) {
        return foodItemService.getFoodItemByCategory(company_id);
    }

}
