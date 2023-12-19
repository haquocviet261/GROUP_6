package com.petshop.controller;

import com.petshop.models.dto.request.CartItemDTO;
import com.petshop.services.imp.ShoppingCartServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
@RestController
@RequestMapping("api/v1/checkout")
public class CheckOutController {
    @Autowired
    ShoppingCartServiceImp shoppingCartServiceImp;
    @PostMapping("/checkout-content")
    public ResponseEntity<?> checkOut(@RequestBody List<CartItemDTO> items, Principal connectedUser){
        return shoppingCartServiceImp.checkOut(items,connectedUser);
    }
}
