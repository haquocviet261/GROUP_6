package com.petshop.controller;

import com.petshop.model.dto.request.OrderDTO;
import com.petshop.services.imp.OrderServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("api/order")
public class OrderController {
    @Autowired
    private OrderServiceImp orderServiceImp;

    @PostMapping("/add")
    public ResponseEntity<?> addOrder(@RequestBody OrderDTO orders, Principal connectedUser){
        return orderServiceImp.addOrder(orders,connectedUser);
    }
}
