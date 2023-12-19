package com.petshop.services.interfaces;

import com.petshop.models.dto.request.OrderDTO;
import com.petshop.models.entities.Orders;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

public interface OrderService {
    public ResponseEntity<String> addOrder(OrderDTO orders, Principal connectedUser);
}
