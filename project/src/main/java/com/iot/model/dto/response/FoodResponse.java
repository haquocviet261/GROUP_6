package com.iot.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodResponse {
    private Long food_id;
    private String food_name;
    private Integer date_expired;
    private Integer calories_per_unit;
    private String unit;
    private Long food_category_id;
}
