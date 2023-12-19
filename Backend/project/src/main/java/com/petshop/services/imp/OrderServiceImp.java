package com.petshop.services.imp;

import com.petshop.models.dto.request.CartItemDTO;
import com.petshop.models.dto.request.OrderDTO;
import com.petshop.models.entities.*;
import com.petshop.repositories.OrderDetailsRepository;
import com.petshop.repositories.OrderRepository;
import com.petshop.repositories.ProductRepository;
import com.petshop.services.interfaces.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.sql.Date;

@Service
public class OrderServiceImp implements OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderDetailsRepository orderDetailsRepository;
    @Autowired
    ProductRepository productRepository;

    @Override
    public ResponseEntity<String> addOrder(OrderDTO request, Principal connectedUser) {
        if (request == null){
            return ResponseEntity.ok("add Order false because Order is null");
        }else {
            var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
            Date date = new Date(System.currentTimeMillis());
            Orders orders = new Orders();
            orders.setOrder_date(date);
            orders.setOrder_status(1);
            orders.setPhone_number(request.getPhone_number());
            orders.setAddress(request.getAddress());
            orders.setFull_name(request.getFullname());
            orders.setUser(user);
            orderRepository.save(orders);
            for (CartItemDTO item:request.getItems()) {
                Object[] product = productRepository.findByProduct_id(item.getProduct_id());
                Product current_product = (Product) product[0];
                OrderDetails orderDetails = OrderDetails.builder().orders(orders).product(current_product)
                        .quantity(item.getQuantity()).total_price(current_product.getPrice()*item.getQuantity()).build();
                orderDetailsRepository.save(orderDetails);
            }
        }
        return ResponseEntity.ok("Add order successfully !");
    }
}
