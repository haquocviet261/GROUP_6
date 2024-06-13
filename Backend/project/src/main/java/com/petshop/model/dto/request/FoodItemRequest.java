package com.petshop.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodItemRequest {
    private String food_name;
    private Date add_date;
    private int quantity;
    private Date expiration_date;
    private Long device_item_id;
    private Long food_id;
}
