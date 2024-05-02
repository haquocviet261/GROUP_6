package com.petshop.services.imp;


import com.petshop.models.dto.request.CartItemDTO;
import com.petshop.models.dto.response.ResponseObject;
import com.petshop.models.entities.CartItem;
import com.petshop.models.entities.ShoppingCart;
import com.petshop.repositories.UserRepository;
import com.petshop.services.interfaces.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShoppingCartServiceImp implements ShoppingCartService{
    private static final String CART_KEY_PREFIX = "cart:";
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public ResponseEntity<ResponseObject> addToCart(Long user_id, CartItem cartItem) {
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        String cartKey = CART_KEY_PREFIX + user_id;

        if (!userRepository.findById(user_id).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("False", "Add to Cart Failed: User does not exist", null));
        }

        String productKey = String.valueOf(cartItem.getProduct_id());
        String quantity = String.valueOf(cartItem.getQuantity());

       //check cart exist
        if (hashOps.hasKey(cartKey, productKey)) {
           // if cart is exist
            int currentQuantity = Integer.parseInt(hashOps.get(cartKey, productKey));
            int newQuantity = currentQuantity + cartItem.getQuantity();
            hashOps.put(cartKey, productKey, String.valueOf(newQuantity));
        } else {
           //if item dose not exist
            hashOps.put(cartKey, productKey, quantity);
        }

        List<CartItem> cartItems = new ArrayList<>();
        for (Map.Entry<String, String> entry : hashOps.entries(cartKey).entrySet()) {
            cartItems.add(new CartItem(Long.parseLong(entry.getKey()), Integer.parseInt(entry.getValue())));
        }

        return ResponseEntity.ok(new ResponseObject("OK", "Add to Cart successfully",
                new HashMap<String, Object>() {{
                    put("user_id", user_id);
                    put("cart_items", cartItems);
                }}));
    }


    @Override
    public ResponseEntity<ResponseObject> getCart(Long user_id) {
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        String cartKey = CART_KEY_PREFIX + user_id;

        if (!userRepository.findById(user_id).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("False", "Get Cart Failed: User does not exist", null));
        }

        List<CartItem> cartItems = new ArrayList<>();
        Map<String, String> cartData = hashOps.entries(cartKey);
        for (Map.Entry<String, String> entry : cartData.entrySet()) {
            long productId = Long.parseLong(entry.getKey());
            int quantity = Integer.parseInt(entry.getValue());
            cartItems.add(new CartItem(productId, quantity));
        }

        return ResponseEntity.ok(new ResponseObject("OK", "Get Cart successfully",
                new HashMap<String, Object>() {{
                    put("user_id", user_id);
                    put("cart_items", cartItems);
                }}));
    }


    @Override
    public ResponseEntity<ResponseObject> RemoveItemFromCart(Long user_id, Long product_id) {
        String cartKey = CART_KEY_PREFIX + user_id;
        if (userRepository.findById(user_id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("False","Remove Product from Cart False, user is not exist !",null));
        }
        CartItem cartItem = (CartItem) redisTemplate.opsForHash().entries(CART_KEY_PREFIX);
        if (redisTemplate.opsForHash().delete(cartKey,product_id) > 0){
            return ResponseEntity.ok(new ResponseObject("OK","Removed Product from Cart successfully !",cartItem));
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("False","Removed Product from Cart false !",cartItem));
        }

    }

    @Override
    public ResponseEntity<ResponseObject> RemoveAddCart(Long user_id) {
        String cartKey = CART_KEY_PREFIX + user_id;
        if (userRepository.findById(user_id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("False","Remove from Cart False user is not exist !",""));
        }

        if (Boolean.TRUE.equals(redisTemplate.delete(cartKey))){
            return ResponseEntity.ok(new ResponseObject("OK","Removed  Cart successfully !",""));
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("False","Removed  Cart false !",""));
        }

    }

    @Override
    public ResponseEntity<ResponseObject> DecreaseQuantityItemFromCart(Long user_id, CartItem cartItem) {
        HashOperations<String, Long, Integer> hashOps = redisTemplate.opsForHash();
        String cartKey = CART_KEY_PREFIX + user_id;
        if (!userRepository.findById(user_id).isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("False","Add to Cart False user is not exist",""));
        }
        if (redisTemplate.opsForHash().hasKey(cartKey,cartItem.getProduct_id())){
            int current_quantity = (Integer) redisTemplate.opsForHash().get(cartKey,cartItem.getProduct_id());
            if (current_quantity > 0){
                hashOps.put(cartKey, cartItem.getProduct_id(),(current_quantity - 1));
            }else {
                return ResponseEntity.ok(new ResponseObject("OK","False to decrease quantity !",hashOps.entries(cartKey)));
            }
        }
        hashOps.put(cartKey, cartItem.getProduct_id(), cartItem.getQuantity());
        return ResponseEntity.ok(new ResponseObject("OK","Add to Cart successfully",hashOps.entries(cartKey)));
    }

}
