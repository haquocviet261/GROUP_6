package com.petshop.models.dto.request;

import com.petshop.models.entities.Product;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private Long product_id;
    private int quantity;
    private double price;
}
