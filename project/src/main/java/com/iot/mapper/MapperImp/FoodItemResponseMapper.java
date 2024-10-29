package com.iot.mapper.MapperImp;

import com.iot.mapper.Mapper;

import com.iot.model.dto.response.FoodItemResponse;
import com.iot.model.entity.FoodItem;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Component
public class FoodItemResponseMapper implements Mapper<FoodItem, FoodItemResponse> {
    private final ModelMapper modelMapper;

    public FoodItemResponseMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public FoodItemResponse mapTo(FoodItem foodItem) {
        FoodItemResponse response = modelMapper.map(foodItem, FoodItemResponse.class);

        response.setUnit(foodItem.getQuantity() + foodItem.getType_unit());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(foodItem.getCreated_at());
        calendar.add(Calendar.DAY_OF_YEAR, foodItem.getExpired_date());
        response.setExpiration_date(calendar.getTime());

        return response;
    }

    @Override
    public FoodItem mapFrom(FoodItemResponse foodItemResponse) {
        return null;
    }

    public List<FoodItemResponse> mapListTo(List<FoodItem> foodItemList) {
        List<FoodItemResponse> foodItemResponses = new ArrayList<>();
        for (FoodItem foodItem : foodItemList) {
            foodItemResponses.add(mapTo(foodItem));
        }
        return foodItemResponses;
    }
}
