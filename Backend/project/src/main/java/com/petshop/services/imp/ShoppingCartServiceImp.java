package com.petshop.services.imp;


import com.petshop.models.dto.response.ResponseObject;
import com.petshop.models.entities.CartItem;
import com.petshop.models.entities.ShoppingCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.HashMap;
@Service
public class ShoppingCartServiceImp {
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    public ResponseEntity<ResponseObject> getCart(Long userId) {
        return ResponseEntity.ok(new ResponseObject("OK","List cart",redisTemplate.opsForValue().get(userId)));
    }
    public ResponseEntity<String> addToCart(Long userId, CartItem cartItem) {
        ShoppingCart shoppingCart = (ShoppingCart) redisTemplate.opsForValue().get(userId);

        if (shoppingCart == null) {
            shoppingCart = new ShoppingCart(userId, new HashMap<>());
        }
        if (shoppingCart.getItems().get(cartItem.getProduct_id()) !=null){
            int quantity = shoppingCart.getItems().get(cartItem.getProduct_id()).getQuantity();
            shoppingCart.getItems().get(cartItem.getProduct_id()).setQuantity(quantity + cartItem.getQuantity());
        }
        shoppingCart.getItems().put(cartItem.getProduct_id(), shoppingCart.getItems().get(cartItem.getProduct_id()));
        redisTemplate.opsForValue().set(userId, shoppingCart);
        return ResponseEntity.ok("Add to cart successfully");
    }
    public void removeFromCart(Long userId, Long productId) {
        ShoppingCart shoppingCart = (ShoppingCart) redisTemplate.opsForValue().get(userId);

        if (shoppingCart != null) {
            shoppingCart.getItems().remove(productId);
            redisTemplate.opsForValue().set(userId, shoppingCart);
        }
    }
}
