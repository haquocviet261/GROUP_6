package com.petshop.models.dto.response;

import com.petshop.models.dto.request.CartItemDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckOutResponse {
    private List<CartItemDTO> items;
    private double total;
}
