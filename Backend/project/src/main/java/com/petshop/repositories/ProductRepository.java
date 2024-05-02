package com.petshop.repositories;

import com.petshop.models.dto.response.DiscountProductResponse;
import com.petshop.models.entities.Product;
import jakarta.persistence.*;
import org.hibernate.annotations.NamedNativeQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;


public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query(value = "SELECT p.product_id, p.sub_category_id, p.product_name, p.quantity, p.price, " +
            "p.description, p.product_image, d.discount_value, s.category_id " +
            "FROM products p LEFT JOIN discount d ON p.discount_id = d.discount_id " +
            "INNER JOIN sub_categories s ON p.sub_category_id = s.sub_category_id " +
            "ORDER BY NEWID()", nativeQuery = true)
    List<Object[]> findRandomProducts(Pageable pageable);
    @Query("SELECT p,d.discount_value,p.subCategory.category.category_id FROM Product p LEFT JOIN p.discount d  WHERE  p.subCategory.category.category_id IN (SELECT s.category.category_id FROM SubCategory s WHERE s.category.category_id = :category_id)")
    List<Object[]> findProductByCategory(Pageable pageable,@Param("category_id") Long category_id);

    @Override
    List<Product> findAll();

    @Query("SELECT p,sc.discount_value, p.subCategory.category.category_id FROM Product p JOIN p.discount sc   ORDER BY sc.discount_value DESC")
    List<Object[]> findTopSaleProduct();

    @Query("SELECT p, d.discount_value,p.subCategory.category.category_id FROM Product p LEFT JOIN p.discount d WHERE p.subCategory.sub_category_id = :sub_category_id ")
    List<Object[]> findProductBySubcategoryId(@Param("sub_category_id") Long sub_category_id);
    @Query("select p, d.discount_value,p.subCategory.category.category_id from Product p LEFT JOIN p.discount d  where p.product_name like %:product_name% ")
    List<Object[]> findByNameContainingIgnoreCase(Pageable pageable,@Param("product_name") String product_name);
    @Query("select p, d.discount_value,p.subCategory.category.category_id from Product p LEFT JOIN p.discount d  where p.product_name like %:product_name% ")
    List<Object[]> findByNameContainingIgnoreCase(@Param("product_name") String product_name);
    @Query("select p,d.discount_value,p.subCategory.category.category_id from Product p join p.subCategory s LEFT JOIN p.discount d  where p.subCategory.sub_category_name like %:sub_category_name% ")
    List<Object[]> findBySubCategoriesContainingIgnoreCase(Pageable pageable,@Param("sub_category_name") String sub_category_name);
    @Query("select p,d.discount_value,p.subCategory.category.category_id from Product p LEFT JOIN p.discount d where p.product_id = :product_id ")
    List<Object[]> findByProduct_id(@Param("product_id") Long product_id);
   @Query("SELECT p, d.discount_value,p.subCategory.category.category_id FROM Product p LEFT JOIN p.discount d")
   List<Object[]> getProductAndDiscount();
}
