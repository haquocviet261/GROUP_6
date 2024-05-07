package com.petshop.services.interfaces;


import com.petshop.model.dto.request.OrderDTO;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

public interface OrderService {
    public ResponseEntity<String> addOrder(OrderDTO orders, Principal connectedUser);
}
