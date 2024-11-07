package com.iot.services.interfaces;

import com.iot.model.dto.response.ResponseObject;
import com.iot.model.entity.Food;
import org.springframework.http.ResponseEntity;

public interface FoodService {
    ResponseEntity<ResponseObject> getAllFood();
    ResponseEntity<ResponseObject> getFoodByName(String name);
    ResponseEntity<ResponseObject> getFoodByCategoryId(Integer category_id);
    ResponseEntity<ResponseObject> addFood(Food food);
    ResponseEntity<ResponseObject> deleteFood(Integer id);
    ResponseEntity<ResponseObject> updateFood(Long id, Food food);
    ResponseEntity<ResponseObject> findByFoodId(Integer id);
    ResponseEntity<ResponseObject> getListFoodNameByKeyword(String keyword);

}
