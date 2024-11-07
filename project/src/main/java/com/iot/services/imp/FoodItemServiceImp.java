package com.iot.services.imp;

import com.iot.model.dto.request.FoodItemRequest;
import com.iot.model.dto.response.ResponseObject;
import com.iot.model.entity.FoodItem;
import com.iot.repositories.FoodItemRepository;
import com.iot.services.interfaces.FoodItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
@Service

public class FoodItemServiceImp implements FoodItemService {
    @Autowired
    private FoodItemRepository foodItemRepository;
    @Override
    public ResponseEntity<ResponseObject> getAllFoodItem() {
        return null;
    }

    @Override
    public ResponseEntity<ResponseObject> getExpiredFoodItems(Long company_id) {
        List<FoodItem> foodItems = foodItemRepository.findByCompanyId(company_id);
        List<FoodItem> expiredItems = new ArrayList<>();

        Date currentDate = new Date();

        for (FoodItem foodItem : foodItems) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(foodItem.getCreated_at());
            cal.add(Calendar.DAY_OF_MONTH, foodItem.getExpired_date());

            if (cal.getTime().before(currentDate)) {
                expiredItems.add(foodItem);
            }
        }
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.toString(), "Get food expired successfully !", expiredItems));
    }

    @Override
    public ResponseEntity<ResponseObject> addFoodItem(FoodItemRequest foodItemRequest) {
        return null;
    }
}
