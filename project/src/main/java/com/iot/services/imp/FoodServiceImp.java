package com.iot.services.imp;

import com.iot.common.utils.CommonUtils;
import com.iot.common.utils.Validation;
import com.iot.model.dto.response.FoodResponse;
import com.iot.model.dto.response.ResponseObject;
import com.iot.model.entity.Food;
import com.iot.repositories.FoodRepository;
import com.iot.services.interfaces.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FoodServiceImp implements FoodService {
    @Autowired
    private FoodRepository foodRepository;

    @Override
    public ResponseEntity<ResponseObject> getAllFood() {
        return ResponseEntity.ok(new ResponseObject(Validation.OK, "List All Food", foodRepository.getAllFood()));
    }

    @Override
    public ResponseEntity<ResponseObject> getFoodByName(String name) {
        Optional<FoodResponse> optionalFood = foodRepository.findFoodByName(name);
        return optionalFood.map(food -> ResponseEntity.ok(new ResponseObject(Validation.OK, "Successfully!!", food)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseObject(Validation.FAIL, "Not found!!!", null)));
    }

    @Override
    public ResponseEntity<ResponseObject> getFoodByCategoryId(Integer category_id) {
        List<FoodResponse> foodList = foodRepository.findFoodByCategoryId(category_id);
        if (foodList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject(Validation.FAIL, "category_id  " + category_id + " is not found!!!", null));
        }
        return ResponseEntity.ok(new ResponseObject(Validation.OK, "Successfully!!!", foodList));
    }

    @Override
    public ResponseEntity<ResponseObject> addFood(Food food) {
        Optional<Food> foodOptional = foodRepository.findByFoodName(food.getName());
        if(foodOptional.isPresent()){
            ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ResponseObject(Validation.FAIL, "Food_name is exist!!!", null));
        }
        return ResponseEntity.ok(new ResponseObject(Validation.OK, "Successfully!!!", foodRepository.save(food)));
    }

    @Override
    public ResponseEntity<ResponseObject> deleteFood(Integer id) {
        Optional<Food> optionalFood = foodRepository.findById(id);
        if (optionalFood.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject(Validation.FAIL, "Fail!!!", null));
        }
        Food food = optionalFood.get();
        food.setDeleted_at(new Date());
        return ResponseEntity.ok(new ResponseObject(Validation.OK, "Deleted successfully!!!", foodRepository.save(food)));
    }

    @Override
    public ResponseEntity<ResponseObject> updateFood(Long id, Food newFood) {
        Optional<Food> optionalFood = foodRepository.findById(id);
        if (optionalFood.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject(Validation.FAIL, "Fail!!!", null));
        }
        Food food = optionalFood.get();
        food.setName(newFood.getName());
        food.setCategory_id(newFood.getCategory_id());
        food.setExpired_date(newFood.getExpired_date());
        food.setUpdated_by(CommonUtils.getUserInformationLogin().getUser_name());
        return ResponseEntity.ok(new ResponseObject(Validation.OK, "Updated successfully!!!", foodRepository.save(food)));
    }

    @Override
    public ResponseEntity<ResponseObject> findByFoodId(Integer id) {
        Optional<Food> optionalFood = foodRepository.findById(id);
        return optionalFood.map(food -> ResponseEntity.ok(new ResponseObject(Validation.OK, "Successfully!!!", food)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseObject(Validation.FAIL, "Not Found!!!", null)));
    }

    @Override
    public ResponseEntity<ResponseObject> getListFoodNameByKeyword(String keyword) {
        return ResponseEntity.ok(new ResponseObject(Validation.OK, "Success !!!", foodRepository.getFoodNameByKeyword(keyword)));
    }
}
