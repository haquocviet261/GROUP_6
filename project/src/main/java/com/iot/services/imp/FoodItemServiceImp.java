package com.iot.services.imp;

import com.iot.common.constant.CommonConstant;
import com.iot.common.utils.CommonUtils;
import com.iot.common.utils.Validation;
import com.iot.mapper.MapperImp.FoodItemResponseMapper;
import com.iot.model.dto.request.FoodItemRequest;
import com.iot.model.dto.response.ResponseObject;
import com.iot.model.entity.Food;
import com.iot.model.entity.FoodItem;
import com.iot.model.entity.Notification;
import com.iot.model.entity.User;
import com.iot.repositories.FoodItemRepository;
import com.iot.repositories.FoodRepository;
import com.iot.repositories.NotificationRepository;
import com.iot.repositories.UserRepository;
import com.iot.services.interfaces.FoodItemService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FoodItemServiceImp implements FoodItemService {
    @Autowired
    FoodItemRepository foodItemRepository;
    @Autowired
    FoodItemResponseMapper foodItemResponseMapper;
    @Autowired
    FoodRepository foodRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserRepository userRepository;


    @Override
    public ResponseEntity<ResponseObject> getAllFoodItem() {
        User user = CommonUtils.getUserInformationLogin();
        List<FoodItem> foodItemList = foodItemRepository.getAllFoodItem(user.getCompany_id());
        return ResponseEntity.ok(
                new ResponseObject(Validation.OK, "Successfully !!!", foodItemResponseMapper.mapListTo(foodItemList)));
    }

    @Override
    public ResponseEntity<ResponseObject> addFoodItem(FoodItemRequest foodItemRequest) {
        FoodItem foodItem = new FoodItem();

        Optional<Food> optionalFood = foodRepository.findByFoodName(foodItemRequest.getName());
        if (optionalFood.isEmpty()) {
            Food food = new Food();
            food.setName(foodItemRequest.getName());
            food.setExpired_date(foodItemRequest.getExpired_date());
            food.setCategory_id(4);
            foodRepository.save(food);
            foodItem.setFood_id(Math.toIntExact(food.getId()));
            foodItem.setCategoryId(food.getCategory_id());
        } else {
            foodItem.setFood_id(Math.toIntExact(optionalFood.get().getId()));
            foodItem.setCategoryId(optionalFood.get().getCategory_id());
        }
        foodItem.setType_unit("kg");
        BeanUtils.copyProperties(foodItemRequest, foodItem);
        foodItemRepository.save(foodItem);
        return ResponseEntity
                .ok(new ResponseObject(Validation.OK, "Successfully !!!", foodItemResponseMapper.mapTo(foodItem)));
    }

    @Override
    public ResponseEntity<ResponseObject> updateFoodItem(Long id, FoodItemRequest foodItemRequest) {
        Optional<FoodItem> optionalFoodItem = foodItemRepository.findById(id);
        if (optionalFoodItem.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject(Validation.FAIL, "Fail!!!", null));
        }

        FoodItem foodItem = optionalFoodItem.get();
        String foodName = foodItemRequest.getName();
        if (!(foodName == null || foodName.trim().isEmpty())) {
            Optional<Food> optionalFood = foodRepository.findByFoodName(foodName);
            if (optionalFood.isEmpty()) {
                Food food = new Food();
                food.setName(foodName);
                food.setExpired_date(foodItemRequest.getExpired_date());
                food.setCategory_id(4); // id=4:other
                foodRepository.save(food);
                foodItem.setFood_id(Math.toIntExact(food.getId()));
                foodItem.setCategoryId(food.getCategory_id());
            } else {
                foodItem.setFood_id(Math.toIntExact(optionalFood.get().getId()));
                foodItem.setCategoryId(optionalFood.get().getCategory_id());
            }
        }
        BeanUtils.copyProperties(foodItemRequest, foodItem);
        foodItem.setUpdated_at(new Date());
        foodItem.setUpdated_by(CommonUtils.getUserInformationLogin().getUser_name());

        String message = "Food Item " + foodItem.getName() + " has been updated by @" + foodItem.getUpdated_by() + "!";
        saveNotifications(message, foodItem.getCompanyId());

        return ResponseEntity
                .ok(new ResponseObject(Validation.OK, "Updated successfully !!!", foodItemRepository.save(foodItem)));
    }

    private void saveNotifications(String message, Integer companyId) {
        List<User> employees = userRepository.findByCompanyId(companyId);
        List<Notification> notifications = new ArrayList<>();
        for (User employee : employees) {
            notifications.add(Notification.builder().type_notification(CommonConstant.UPDATE_FOOD_ITEM)
                    .message(message).userId(employee.getId()).status("unread").build());
        }
        notificationRepository.saveAll(notifications);
    }

    @Override
    public ResponseEntity<ResponseObject> clearDataFoodItem(Long id) {
        Optional<FoodItem> optionalFoodItem = foodItemRepository.findById(id);
        if (optionalFoodItem.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject(Validation.FAIL, "Fail!!!", null));
        }
        FoodItem foodItem = optionalFoodItem.get();
        foodItem.setFood_id(null);
        foodItem.setName(null);
        foodItem.setExpired_date(null);
        foodItem.setUpdated_at(new Date());
        foodItemRepository.save(foodItem);
        return ResponseEntity.ok(new ResponseObject(Validation.FAIL, "Clear data successfully !!!", null));
    }

    @Override
    public ResponseEntity<ResponseObject> searchFoodItem(String keyword) {
        User user = CommonUtils.getUserInformationLogin();
        List<FoodItem> foodItemList = foodItemRepository.searchFoodItemByName(keyword,user.getCompany_id());
        return ResponseEntity.ok(
                new ResponseObject(Validation.OK, "Successfully !!!", foodItemResponseMapper.mapListTo(foodItemList)));
    }

    @Override
    public ResponseEntity<ResponseObject> getFoodItemByCategory(Integer category_id) {
        User user = CommonUtils.getUserInformationLogin();
        return ResponseEntity.ok(new ResponseObject(Validation.OK, "Successfully !!!",
                foodItemResponseMapper.mapListTo(foodItemRepository.getListFoodItemByCategory(category_id,user.getCompany_id()))));
    }

    @Override
    public ResponseEntity<ResponseObject> getFoodItemById(Integer id) {
        Optional<FoodItem> optionalFoodItem = foodItemRepository.getFoodItemById(id);
        return optionalFoodItem.map(foodItem -> ResponseEntity.ok(new ResponseObject(Validation.OK, "Successfully !!!",
                foodItemResponseMapper.mapTo(foodItem))))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseObject(Validation.FAIL, "Fail !!!", "")));
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
        return ResponseEntity
                .ok(new ResponseObject(HttpStatus.OK.toString(), "Get food expired successfully !", expiredItems));
    }
}