package com.iot.services.imp;

import com.iot.common.utils.CommonUtils;
import com.iot.common.utils.Validation;
import com.iot.mapper.MapperImp.FoodItemResponseMapper;
import com.iot.model.dto.request.FoodItemRequest;
import com.iot.model.dto.response.FoodItemResponse;
import com.iot.model.dto.response.ResponseObject;
import com.iot.model.entity.Food;
import com.iot.model.entity.FoodItem;
import com.iot.repositories.FoodItemRepository;
import com.iot.repositories.FoodRepository;
import com.iot.services.interfaces.FoodItemService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class FoodItemServiceImp implements FoodItemService {
    @Autowired
    FoodItemRepository foodItemRepository;
    @Autowired
    FoodItemResponseMapper foodItemResponseMapper;
    @Autowired
    FoodRepository foodRepository;


    private List<FoodItemResponse> getAllFoodItemDTO() {
        List<FoodItem> foodItemList = foodItemRepository.getAllFoodItem();
        return foodItemResponseMapper.mapListTo(foodItemList);
    }

    @Override
    public ResponseEntity<ResponseObject> getAllFoodItem() {
        List<FoodItemResponse> foodItemResponses = getAllFoodItemDTO();
        return ResponseEntity.ok(new ResponseObject(Validation.OK, "Successfully !!!", foodItemResponses));
    }

    @Override
    public ResponseEntity<ResponseObject> getAllFoodItemSortByNameAsc() {
        List<FoodItemResponse> foodItemResponses = getAllFoodItemDTO();
        foodItemResponses.sort(Comparator.comparing(FoodItemResponse::getName));
        return ResponseEntity.ok(new ResponseObject(Validation.OK, "Successfully !!!", foodItemResponses));
    }

    @Override
    public ResponseEntity<ResponseObject> getAllFoodItemSortByNameDesc() {
        List<FoodItemResponse> foodItemResponses = getAllFoodItemDTO();
        foodItemResponses.sort(Comparator.comparing(FoodItemResponse::getName).reversed());
        return ResponseEntity.ok(new ResponseObject(Validation.OK, "Successfully !!!", foodItemResponses));
    }

    @Override
    public ResponseEntity<ResponseObject> getAllFoodItemSortByExpirationDateAsc() {
        List<FoodItemResponse> foodItemResponses = getAllFoodItemDTO();
        foodItemResponses.sort(Comparator.comparing(FoodItemResponse::getExpiration_date));
        return ResponseEntity.ok(new ResponseObject(Validation.OK, "Successfully !!!", foodItemResponses));
    }

    @Override
    public ResponseEntity<ResponseObject> getAllFoodItemSortByExpirationDateDesc() {
        List<FoodItemResponse> foodItemResponses = getAllFoodItemDTO();
        foodItemResponses.sort(Comparator.comparing(FoodItemResponse::getExpiration_date).reversed());
        return ResponseEntity.ok(new ResponseObject(Validation.OK, "Successfully !!!", foodItemResponses));
    }

    @Override
    public ResponseEntity<ResponseObject> addFoodItem(FoodItemRequest foodItemRequest) {
        FoodItem foodItem = new FoodItem();

        Optional<Food> optionalFood = foodRepository.findByFoodName(foodItemRequest.getName());
        if (optionalFood.isEmpty()) {
            Food food = new Food();
            food.setName(foodItemRequest.getName());
            food.setExpired_date(foodItemRequest.getExpired_date());
            food.setCategory_id(4); // id=4:other
            foodRepository.save(food);
            foodItem.setFood_id(4);
        } else {
            foodItem.setFood_id(Math.toIntExact(optionalFood.get().getId()));
        }

        BeanUtils.copyProperties(foodItemRequest, foodItem);
        foodItemRepository.save(foodItem);
        return ResponseEntity.ok(new ResponseObject(Validation.OK, "Successfully !!!", foodItemResponseMapper.mapTo(foodItem)));
    }

    @Override
    public ResponseEntity<ResponseObject> updateFoodItem(Long id, FoodItemRequest foodItemRequest) {
        Optional<FoodItem> optionalFoodItem = foodItemRepository.findById(id);
        if (optionalFoodItem.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject(Validation.FAIL, "Fail!!!", null));
        }

        FoodItem foodItem = optionalFoodItem.get();

        Optional<Food> optionalFood = foodRepository.findByFoodName(foodItemRequest.getName());
        if (optionalFood.isEmpty()) {
            Food food = new Food();
            food.setName(foodItemRequest.getName());
            food.setExpired_date(foodItemRequest.getExpired_date());
            food.setCategory_id(4); // id=4:other
            foodRepository.save(food);
            foodItem.setFood_id(4);
        } else {
            foodItem.setFood_id(Math.toIntExact(optionalFood.get().getId()));
        }
        BeanUtils.copyProperties(foodItemRequest, foodItem);
        foodItem.setUpdated_by(CommonUtils.getUserInforLogin().getUser_name());
        return ResponseEntity.ok(new ResponseObject(Validation.OK, "Updated successfully !!!", foodItemRepository.save(foodItem)));
    }

    @Override
    public ResponseEntity<String> clearDataFoodItem(Long id) {
        Optional<FoodItem> optionalFoodItem = foodItemRepository.findById(id);
        if (optionalFoodItem.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fail!!!");
        }
        FoodItem foodItem = optionalFoodItem.get();
        foodItem.setFood_id(null);
        foodItem.setName(null);
        foodItem.setQuantity(null);
        foodItem.setType_unit(null);
        foodItem.setExpired_date(null);
        foodItemRepository.save(foodItem);
        return ResponseEntity.ok("Clear data successfully !!!");
    }

    @Override
    public ResponseEntity<ResponseObject> searchFoodItem(String keyword) {
        List<FoodItem> foodItemList = foodItemRepository.searchFoodItemByName(keyword);
        return ResponseEntity.ok(new ResponseObject(Validation.OK, "Successfully !!!", foodItemResponseMapper.mapListTo(foodItemList)));
    }
}
