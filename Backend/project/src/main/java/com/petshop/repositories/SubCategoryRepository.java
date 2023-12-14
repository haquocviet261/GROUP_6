package com.petshop.repositories;

import com.petshop.models.entities.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubCategoryRepository extends JpaRepository<SubCategory,Long> {
    @Query("select s from SubCategory s  where s.category.category_id = :category_id ")
    public List<SubCategory> findSubcategoryByCategoryId(@Param("category_id") Long category_id );
    public List<SubCategory> findAll();
}
