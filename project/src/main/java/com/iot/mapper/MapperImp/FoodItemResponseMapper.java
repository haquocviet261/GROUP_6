package com.iot.mapper.MapperImp;

import com.iot.common.utils.Validation;
import com.iot.mapper.Mapper;

import com.iot.model.dto.response.FoodItemResponse;
import com.iot.model.entity.FoodItem;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
        //Set Expiration date
        if (foodItem.getExpired_date() == null) {
            response.setExpiration_date(null);
        } else {
            response.setExpiration_date(Validation.calculateExpirationDate(foodItem.getExpired_date(),foodItem.getUpdated_at()));
        }
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
