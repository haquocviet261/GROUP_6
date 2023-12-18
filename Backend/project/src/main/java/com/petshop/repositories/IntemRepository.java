package com.petshop.repositories;

import com.petshop.models.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntemRepository extends JpaRepository<Item,Long> {

}
