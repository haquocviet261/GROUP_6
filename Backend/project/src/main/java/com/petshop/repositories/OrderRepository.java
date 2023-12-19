package com.petshop.repositories;

import com.petshop.models.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Orders,Long> {

}
