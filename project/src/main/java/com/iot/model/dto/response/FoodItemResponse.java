package com.iot.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FoodItemResponse {
    private String name;
    private String unit;
    private Date expiration_date;
    private Integer device_id;
}
