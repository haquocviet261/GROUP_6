package com.petshop.repositories;

import com.petshop.models.entities.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails,Long> {

}
