package com.petshop.controller;

import com.petshop.models.dto.response.ResponseObject;
import com.petshop.models.entities.CartItem;
import com.petshop.models.entities.ShoppingCart;
import com.petshop.models.entities.User;
import com.petshop.services.imp.ShoppingCartServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/cart")
public class RedisController {
    @Autowired
    private ShoppingCartServiceImp shoppingCartService;

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseObject> getCart(@PathVariable Long userId) {
        return shoppingCartService.getCart(userId);
    }

    @PostMapping("/add/{user_id}")
    public ResponseEntity<String> addToCart(@RequestBody CartItem cartItem,@PathVariable Long user_id) {
       return shoppingCartService.addToCart(user_id, cartItem);
    }

    @DeleteMapping("/remove/{userId}/{productId}")
    public void removeFromCart(@PathVariable Long userId, @PathVariable Long productId) {
        shoppingCartService.removeFromCart(userId, productId);
    }
}
