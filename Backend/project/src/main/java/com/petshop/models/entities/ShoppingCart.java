package com.petshop.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCart implements Serializable {
    private Long  user_id;
    private Map<Long, CartItem> items = new HashMap<>();
}
