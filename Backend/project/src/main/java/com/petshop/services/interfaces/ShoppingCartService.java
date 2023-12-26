package com.petshop.services.interfaces;

import com.petshop.models.dto.request.CartItemDTO;
import com.petshop.models.dto.response.ResponseObject;
import com.petshop.models.entities.Cart;
import com.petshop.models.entities.Item;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;

public interface ShoppingCartService {
    ResponseEntity<ResponseObject> getShoppingCartByUserId(Principal user);
    public ResponseEntity<String> addToCart(List<CartItemDTO> items, Principal user);

    public ResponseEntity<String> removeItemFromCart(Long itemId, Principal user);

    public ResponseEntity<ResponseObject> checkOut(List<CartItemDTO> item,Principal connectedUser);
}
