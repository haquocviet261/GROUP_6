package com.petshop.services.interfaces;

import com.petshop.models.dto.request.CartItemDTO;
import com.petshop.models.dto.response.ResponseObject;
import com.petshop.models.entities.CartItem;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;

public interface ShoppingCartService {
    public ResponseEntity<ResponseObject> addToCart(Long user_id,CartItem cartItem);
    public ResponseEntity<ResponseObject> getCart(Long user_id);
    public ResponseEntity<ResponseObject> RemoveItemFromCart(Long user_id,Long product_id);
    public ResponseEntity<ResponseObject> RemoveAddCart(Long user_id);
    public ResponseEntity<ResponseObject> DecreaseQuantityItemFromCart(Long user_id,CartItem cartItem);
}
