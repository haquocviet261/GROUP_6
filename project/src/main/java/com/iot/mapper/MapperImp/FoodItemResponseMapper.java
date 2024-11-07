package com.iot.mapper.MapperImp;

import com.iot.mapper.Mapper;

import com.iot.model.dto.response.FoodItemResponse;
import com.iot.model.entity.FoodItem;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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

        if (foodItem.getType_unit() == null) {
            response.setUnit(null);
        } else {
            response.setUnit(foodItem.getQuantity() + foodItem.getType_unit());
        }

        if (foodItem.getExpired_date() == null) {
            response.setExpiration_date(null);
        } else {
            LocalDate localDate = foodItem.getUpdated_at().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            LocalDate updatedLocalDate = localDate.plusDays(foodItem.getExpired_date());
            response.setExpiration_date(Date.from(updatedLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
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
