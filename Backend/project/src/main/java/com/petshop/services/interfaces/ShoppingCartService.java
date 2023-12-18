package com.petshop.services.interfaces;

import com.petshop.models.dto.request.CartItemDTO;
import com.petshop.models.entities.Cart;
import com.petshop.models.entities.Item;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ShoppingCartService {
    List<Cart> getShoppingCartByUserId(Long userId);
    public ResponseEntity<String> addToCart(List<CartItemDTO> items, Long user_id);

    public void removeFromCart(Long itemId);
     public void removeCartByProductId(Long product_id);
}
