package com.iot.controller;

import com.iot.model.dto.response.ResponseObject;
import com.iot.model.entity.Food;
import com.iot.services.imp.FoodServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/food")
public class FoodController {
    @Autowired
    private FoodServiceImp foodServiceImp;

    @GetMapping("/all")
    public ResponseEntity<ResponseObject> getAllFood() {
        return foodServiceImp.getAllFood();
    }

    @GetMapping("/category/{category_id}")
    public ResponseEntity<ResponseObject> getFoodByCategoryId(@PathVariable(name = "category_id") Integer category_id) {
        return foodServiceImp.getFoodByCategoryId(category_id);
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseObject> addFood(@RequestBody Food newFood){
        return foodServiceImp.addFood(newFood);
    }

    @PostMapping("/delete/{food_id}")
    public ResponseEntity<ResponseObject> deleteFood(@PathVariable Integer food_id){
        return foodServiceImp.deleteFood(food_id);
    }

    @PostMapping("/update/{food_id}")
    public ResponseEntity<ResponseObject> updateFood(@PathVariable Long food_id,@RequestBody Food food ){
        return foodServiceImp.updateFood(food_id,food);
    }

    @GetMapping("/{food_id}")
    public ResponseEntity<ResponseObject> findFoodByFoodID(@PathVariable Integer food_id) {
        return foodServiceImp.findByFoodId(food_id);
    }
}
