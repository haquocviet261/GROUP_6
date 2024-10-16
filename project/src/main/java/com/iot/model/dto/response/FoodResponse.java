package com.iot.model.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FoodResponse {
    private String name;
    private Integer expired_date;
    private Integer category_id;

    public FoodResponse( String name, Integer expired_date, Integer category_id) {
        this.name = name;
        this.expired_date = expired_date;
        this.category_id = category_id;
    }
}
