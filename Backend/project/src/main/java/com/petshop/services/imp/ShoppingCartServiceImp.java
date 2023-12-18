package com.petshop.services.imp;

import com.petshop.models.dto.request.CartItemDTO;
import com.petshop.models.entities.Item;
import com.petshop.models.entities.Cart;
import com.petshop.models.entities.Product;
import com.petshop.models.entities.User;
import com.petshop.repositories.CartRepository;
import com.petshop.repositories.IntemRepository;
import com.petshop.repositories.ProductRepository;
import com.petshop.repositories.UserRepository;
import com.petshop.services.interfaces.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShoppingCartServiceImp implements ShoppingCartService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    IntemRepository intemRepository;
    @Override
    public List<Cart> getShoppingCartByUserId(Long userId) {
        return null;
    }

    @Override
    public ResponseEntity<String> addToCart(List<CartItemDTO> items, Long user_id) {
        Cart cart = cartRepository.findCartByUserId(user_id);

        if (cart == null) {
            cart = new Cart();
            cart.setUser(userRepository.findById(user_id).orElseThrow());
            cart.setQuantity(0);
            cart.setItems(new ArrayList<>());
        }
        if(cart != null){
            for (CartItemDTO item : items) {
                Product product = productRepository.findById(item.getProduct_id()).orElseThrow();
                Item item_new = new Item();
                item_new.setProduct(product);
                item_new.setQuantity(item.getQuantity());
                item_new.setCart(cart);
                cart.getItems().add(item_new);
                cart.setQuantity(cart.getQuantity()+item.getQuantity());
            }

        }

        return ResponseEntity.ok("Add to cart successfully");
    }

    @Override
    public void removeFromCart(Long itemId) {

    }

    @Override
    public void removeCartByProductId(Long product_id) {

    }
}
