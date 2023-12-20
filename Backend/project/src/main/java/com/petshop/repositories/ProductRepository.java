package com.petshop.repositories;

import com.petshop.models.entities.Product;
import jakarta.persistence.QueryHint;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;


public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query(value = "SELECT TOP 16 * FROM products ORDER BY NEWID()", nativeQuery = true)
    List<Product> findRandomProducts();

    @Override
    List<Product> findAll();

    @Query("SELECT p,sc.discount_value FROM Product p JOIN p.discount sc ORDER BY sc.discount_value DESC")
    List<Object[]> findTopSaleProduct();

    @Query("SELECT p, d.discount_value FROM Product p LEFT JOIN p.discount d  WHERE p.subCategory.sub_category_id = :sub_category_id ")
    List<Object[]> findProductBySubcategoryId(@Param("sub_category_id") Long sub_category_id);
    @Query("select p, d.discount_value from Product p LEFT JOIN p.discount d where p.product_name like %:product_name% ")
    List<Object[]> findByNameContainingIgnoreCase(@Param("product_name") String product_name);
    @Query("select p,d.discount_value from Product p join p.subCategory s LEFT JOIN p.discount d where p.subCategory.sub_category_name like %:sub_category_name% ")
    List<Object[]> findBySubCategoriesContainingIgnoreCase(@Param("sub_category_name") String sub_category_name);
    @Query("select p,d.discount_value from Product p LEFT JOIN p.discount d where p.product_id = :product_id ")
    List<Object[]> findByProduct_id(@Param("product_id") Long product_id);
   @Query("SELECT p, d.discount_value FROM Product p LEFT JOIN p.discount d")
   List<Object[]> getProductAndDiscount();
}
