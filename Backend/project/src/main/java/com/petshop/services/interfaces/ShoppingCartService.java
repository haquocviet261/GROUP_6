package com.petshop.services.interfaces;

import com.petshop.models.dto.request.CartItemDTO;
import com.petshop.models.entities.Cart;
import com.petshop.models.entities.Item;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;

public interface ShoppingCartService {
    Cart getShoppingCartByUserId(Long userId);
    public ResponseEntity<String> addToCart(List<CartItemDTO> items, Long user_id);

    public ResponseEntity<String> removeItemFromCart(Long itemId, Principal user);

     public void removeCartByProductId(Long product_id);
}
