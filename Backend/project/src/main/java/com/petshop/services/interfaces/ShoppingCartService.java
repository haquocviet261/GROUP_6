package com.petshop.services.interfaces;

import com.petshop.models.dto.request.CartItemDTO;
import com.petshop.models.dto.response.ResponseObject;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;

public interface ShoppingCartService {
    ResponseEntity<ResponseObject> getShoppingCartByUserId(Principal user);
    public void addToCart(String productId, int quantity,Principal user);

    public ResponseEntity<String> removeItemFromCart(Long itemId, Principal user);

    public ResponseEntity<ResponseObject> checkOut(List<CartItemDTO> item,Principal connectedUser);
}
