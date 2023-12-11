package com.petshop.repositories;

import com.petshop.models.entities.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriesRepository extends JpaRepository<Categories,Long> {
    public List<Categories> findAll();
}
