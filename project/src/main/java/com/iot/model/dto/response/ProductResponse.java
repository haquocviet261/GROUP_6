package com.iot.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private Long product_id;
    private Long category_id;
    private Long sub_category_id;
    private String product_name;
    private int quantity;
    private double price;
    private String description;
    private String product_image;
    private Double discount_value;
}
