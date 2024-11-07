package com.iot.mapper.MapperImp;

import com.iot.mapper.Mapper;
import com.iot.model.dto.request.FoodItemRequest;
import com.iot.model.entity.FoodItem;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component

public class EditFoodItemMapper implements Mapper<FoodItem, FoodItemRequest> {
    private final ModelMapper modelMapper;

    public EditFoodItemMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public FoodItemRequest mapTo(FoodItem foodItem) {
        return null;
    }

    @Override
    public FoodItem mapFrom(FoodItemRequest foodItemRequest) {
        return modelMapper.map(foodItemRequest, FoodItem.class);
    }
}
