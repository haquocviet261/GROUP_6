package com.iot.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodItemRequest {
    private String name;
    private Integer expired_date;
    private Integer quantity;
    private String type_unit;
}
