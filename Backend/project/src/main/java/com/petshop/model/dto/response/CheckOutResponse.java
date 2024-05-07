package com.petshop.model.dto.response;

import com.petshop.model.dto.request.CartItemDTO;
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
