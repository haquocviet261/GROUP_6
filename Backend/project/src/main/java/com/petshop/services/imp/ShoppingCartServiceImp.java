package com.petshop.services.imp;

import com.petshop.models.dto.request.CartItemDTO;
import com.petshop.models.dto.response.CheckOutResponse;
import com.petshop.models.dto.response.ResponseObject;
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
    public ResponseEntity<ResponseObject> getShoppingCartByUserId(Principal user) {
        var current_user = (User) ((UsernamePasswordAuthenticationToken) user).getPrincipal();
        System.out.println(current_user.getUserId());
        return ResponseEntity.ok(new ResponseObject("OK","List Cart ",cartRepository.findCartByUserId(current_user.getUserId())));
    }

    @Override
    public ResponseEntity<String> addToCart(List<CartItemDTO> items, Principal user) {
        var current_user = (User) ((UsernamePasswordAuthenticationToken) user).getPrincipal();
        Long user_id =current_user.getUserId();
        Cart cart = cartRepository.findCartByUserId(user_id);

        if (cart == null) {
            cart = new Cart();
            cart.setUser(userRepository.findById(user_id).orElseThrow());
            cart.setQuantity(0);
            cart.setItems(new ArrayList<>());
        }
            for (CartItemDTO item : items) {
                List<Object[]> product = productRepository.findByProduct_id(item.getProduct_id());
                Product current_product = (Product) product.get(0)[0];
                Optional<Item> existingItem = findItemByProduct(cart, current_product);
                if (existingItem.isPresent()){
                    Item item_exist = existingItem.get();
                    item_exist.setQuantity(item_exist.getQuantity() + item.getQuantity());
                }else {
                    Item item_new = new Item();
                    item_new.setProduct(current_product);
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
    public ResponseEntity<ResponseObject> checkOut(List<CartItemDTO> items, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Cart cart = cartRepository.findCartByUserId(user.getUserId());
        CheckOutResponse checkOutResponse = new CheckOutResponse();
        checkOutResponse.setItems(items);
        for (CartItemDTO item:items) {
            List<Object[]> product = productRepository.findByProduct_id(item.getProduct_id());
            Product current_product = (Product) product.get(0)[0];
            checkOutResponse.setTotal(checkOutResponse. getTotal()+(current_product.getPrice()*item.getQuantity()));
        }
        return ResponseEntity.ok(new ResponseObject("OK","Check out content", checkOutResponse));
    }
}
