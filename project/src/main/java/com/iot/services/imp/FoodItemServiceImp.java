package com.iot.services.imp;

import com.iot.model.dto.request.FoodItemRequest;
import com.iot.model.dto.response.ResponseObject;
import com.iot.model.entity.DeviceItem;
import com.iot.model.entity.Food;
import com.iot.model.entity.FoodItem;
import com.iot.repositories.DeviceItemRepository;
import com.iot.repositories.FoodItemRepository;
import com.iot.repositories.FoodRepository;
import com.iot.services.interfaces.FoodItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class FoodItemServiceImp implements FoodItemService {
    @Autowired
    private FoodItemRepository foodItemRepository;
    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    DeviceItemRepository deviceItemRepository;

    @Override
    public ResponseEntity<ResponseObject> getAllFoodItem() {
        return ResponseEntity.ok(new ResponseObject("OK","List Food item",foodItemRepository.findAll()));
    }

    @Override
    public ResponseEntity<ResponseObject> addFoodItem(FoodItemRequest foodItemRequest) {
        DeviceItem deviceItem = deviceItemRepository.findById(foodItemRequest.getDevice_item_id()).get();
        Food food = foodRepository.findById(foodItemRequest.getFood_id()).get();
        FoodItem foodItem = new FoodItem();
        foodItem.setFood_name(foodItemRequest.getFood_name());
        foodItem.setAdd_date(foodItemRequest.getAdd_date());
        foodItem.setUnit(String.valueOf(foodItemRequest.getQuantity()));
        foodItem.setExpiration_date(foodItemRequest.getExpiration_date());
        foodItem.setDeviceItem(deviceItem);
        foodItem.setFood(food);

        return  ResponseEntity.ok(new ResponseObject("OK","Add Food successfully",foodItemRepository.save(foodItem)));
    }
    public ResponseEntity<ResponseObject> getFoodItemByUserIDAndDeviceItemID(Long device_item_id){
        return ResponseEntity.ok(new ResponseObject("OK","List food Item",foodItemRepository.getFoodItemByDeviceItemID(device_item_id)));
    }
    public ResponseEntity<ResponseObject> deleteFoodItemByFoodItemId(Long food_item_id){
        foodItemRepository.deleteById(food_item_id);
        return ResponseEntity.ok(new ResponseObject("OK","List food Item",""));
    }
}
