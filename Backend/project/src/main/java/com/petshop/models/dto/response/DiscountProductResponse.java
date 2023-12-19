package com.petshop.models.dto.response;

import com.petshop.models.entities.SubCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscountProductResponse {
    private  Long product_id;
    private Long sub_category_id;
    private String product_name;
    private int quantity;
    private double price;
    private String description;
    private String image;
    private double discount;
}
