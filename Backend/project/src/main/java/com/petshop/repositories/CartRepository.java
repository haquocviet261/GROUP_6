package com.petshop.repositories;

import com.petshop.models.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository  extends JpaRepository<Cart,Long> {
}
