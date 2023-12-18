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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
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
    public Cart getShoppingCartByUserId(Long userId) {
        return cartRepository.findCartByUserId(userId);
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
            for (CartItemDTO item : items) {
                Product product = productRepository.findByProduct_id(item.getProduct_id());
                Optional<Item> existingItem = findItemByProduct(cart, product);
                if (existingItem.isPresent()){
                    Item item_exist = existingItem.get();
                    item_exist.setQuantity(item_exist.getQuantity() + item.getQuantity());
                }else {
                    Item item_new = new Item();
                    item_new.setProduct(product);
                    item_new.setQuantity(item.getQuantity());
                    item_new.setCart(cart);
                    cart.getItems().add(item_new);
                }
                cart.setQuantity(cart.getQuantity() +item.getQuantity());

            }
            cartRepository.save(cart);

        return ResponseEntity.ok("Add to cart successfully");
    }
    private Optional<Item> findItemByProduct(Cart cart, Product product) {
        return cart.getItems().stream()
                .filter(item -> item.getProduct().equals(product))
                .findFirst();
    }

    @Override
    public ResponseEntity<String> removeItemFromCart(Long item_id, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
         Cart cart = cartRepository.findCartByUserId(user.getUserId());
        Optional<Item> existItem = cart.getItems().stream().filter(item -> item.getItem_id() == item_id).findFirst();
        cart.getItems().remove(existItem.orElseThrow(null));
        return ResponseEntity.ok("Remove Item successfully !");
    }

    @Override
    public void removeCartByProductId(Long product_id) {
        Cart cart = cartRepository.getCartByProductId(product_id);
    }
}
