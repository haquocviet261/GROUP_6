package com.petshop.controller;

import com.petshop.services.imp.ShoppingCartServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/checkout")
public class CheckOutController {
    @Autowired
    ShoppingCartServiceImp shoppingCartServiceImp;
//    @PostMapping("/checkout-content")
//    public ResponseEntity<?> checkOut(@RequestBody List<CartItemDTO> items, Principal connectedUser){
//        return shoppingCartServiceImp.checkOut(items,connectedUser);
//    }
}
