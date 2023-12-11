package com.petshop.repositories;

import com.petshop.models.entities.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query(value = "SELECT * FROM product ORDER BY RAND() LIMIT 30", nativeQuery = true)
    List<Product> findRandomProducts();

    @Override
    List<Product> findAll();

    @Query("SELECT p FROM Product p JOIN p.discount sc ORDER BY sc.discount_value DESC")
    List<Product> findTopSaleProduct();

    @Query("SELECT p FROM Product p  WHERE p.subCategory = :sub_category_id")
    List<Product> findProductBySubcategoryId(@Param("sub_category_id") Long sub_category_id);

}
