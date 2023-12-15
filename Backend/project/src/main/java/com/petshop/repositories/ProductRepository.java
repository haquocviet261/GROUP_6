package com.petshop.repositories;

import com.petshop.models.entities.Product;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query(value = "SELECT TOP 30 * FROM products ORDER BY NEWID()", nativeQuery = true)
    List<Product> findRandomProducts();

    @Override
    List<Product> findAll();

    @Query("SELECT p FROM Product p JOIN p.discount sc ORDER BY sc.discount_value DESC")
    List<Product> findTopSaleProduct();

    @Query("SELECT p FROM Product p  WHERE p.subCategory.sub_category_id = :sub_category_id")
    List<Product> findProductBySubcategoryId(@Param("sub_category_id") Long sub_category_id);
    @Query("select p from Product p where p.product_name like %:product_name%")
    List<Product> findByNameContainingIgnoreCase(@Param("product_name") String product_name);
    @Query("select p from Product p join p.subCategory s where p.subCategory.sub_category_name like %:sub_category_name%")
    List<Product> findBySubCategoriesContainingIgnoreCase(@Param("sub_category_name") String sub_category_name);


}
