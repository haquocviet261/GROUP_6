package com.petshop.repositories;

import com.petshop.model.dto.response.ProductResponse;
import com.petshop.model.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query(value = "SELECT p.product_id,s.category_id, p.sub_category_id, p.product_name, p.quantity, p.price, " +
            "p.description, p.product_image, d.discount_value" +
            "FROM products p LEFT JOIN discount d ON p.discount_id = d.discount_id " +
            "INNER JOIN sub_categories s ON p.sub_category_id = s.sub_category_id " +
            "ORDER BY NEWID()", nativeQuery = true)
    List<Object[]> findRandomProducts(Pageable pageable);

    @Query(" select new com.petshop.model.dto.response.ProductResponse(" +
            "            p.product_id, p.subCategory.category.category_id, p.subCategory.sub_category_id, " +
            "            p.product_name, p.quantity, p.price, p.description, p.image, d.discount_value) FROM Product p LEFT JOIN p.discount d  WHERE  p.subCategory.category.category_id IN (SELECT s.category.category_id FROM SubCategory s WHERE s.category.category_id = :category_id)")
    List<ProductResponse> findProductByCategory(Pageable pageable, @Param("category_id") Long category_id);

    @Query("select new com.petshop.model.dto.response.ProductResponse(" +
            "            p.product_id, p.subCategory.category.category_id, p.subCategory.sub_category_id, " +
            "            p.product_name, p.quantity, p.price, p.description, p.image, d.discount_value)  FROM Product p LEFT JOIN p.discount d  WHERE  p.subCategory.category.category_id IN (SELECT s.category.category_id FROM SubCategory s WHERE s.category.category_id = :category_id)")
    List<ProductResponse> findProductByCategory(@Param("category_id") Long category_id);

    @Override
    List<Product> findAll();

    @Query("select new com.petshop.model.dto.response.ProductResponse(p.product_id, p.subCategory.category.category_id," +
            "p.subCategory.sub_category_id, p.product_name, p.quantity, p.price, p.description, p.image, sc.discount_value)  " +
            "FROM Product p JOIN p.discount sc   ORDER BY sc.discount_value DESC")
    List<ProductResponse> findTopSaleProduct();

    @Query("select new com.petshop.model.dto.response.ProductResponse(p.product_id, p.subCategory.category.category_id, " +
            "p.subCategory.sub_category_id, p.product_name, p.quantity, p.price, p.description, p.image, d.discount_value) " +
            " FROM Product p LEFT JOIN p.discount d WHERE p.subCategory.sub_category_id = :sub_category_id ")
    List<ProductResponse> findProductBySubcategoryId(@Param("sub_category_id") Long sub_category_id);

    @Query("select new com.petshop.model.dto.response.ProductResponse(p.product_id, p.subCategory.category.category_id, " +
            "p.subCategory.sub_category_id, p.product_name, p.quantity, p.price, p.description, p.image, d.discount_value) " +
            " from Product p LEFT JOIN p.discount d  where p.product_name like %:product_name% ")
    List<ProductResponse> findByNameContainingIgnoreCase(Pageable pageable, @Param("product_name") String product_name);

    @Query("select new com.petshop.model.dto.response.ProductResponse(p.product_id, p.subCategory.category.category_id, " +
            "p.subCategory.sub_category_id, p.product_name, p.quantity, p.price, p.description, p.image, d.discount_value) " +
            " from Product p LEFT JOIN p.discount d  where p.product_name like %:product_name% ")
    List<ProductResponse> findByNameContainingIgnoreCase(@Param("product_name") String product_name);

    @Query("select new com.petshop.model.dto.response.ProductResponse(p.product_id," +
            " p.subCategory.category.category_id, p.subCategory.sub_category_id, p.product_name, p.quantity, p.price, p.description, p.image, d.discount_value) " +
            " from Product p join p.subCategory s LEFT JOIN p.discount d  where p.subCategory.sub_category_name like %:sub_category_name% ")
    List<ProductResponse> findBySubCategoriesContainingIgnoreCase(@Param("sub_category_name") String sub_category_name);

    @Query("select new com.petshop.model.dto.response.ProductResponse(p.product_id, p.subCategory.category.category_id, " +
            "p.subCategory.sub_category_id, p.product_name, p.quantity, p.price, p.description, p.image, d.discount_value) " +
            " from Product p join p.subCategory s LEFT JOIN p.discount d  where p.subCategory.sub_category_name like %:sub_category_name% ")
    List<ProductResponse> findBySubCategoriesContainingIgnoreCase(Pageable pageable, @Param("sub_category_name") String sub_category_name);

    @Query("select new com.petshop.model.dto.response.ProductResponse(p.product_id, p.subCategory.category.category_id, p.subCategory.sub_category_id, p.product_name, p.quantity, p.price, p.description, p.image, d.discount_value) " +
            " from Product p LEFT JOIN p.discount d where p.product_id = :product_id ")
    ProductResponse findByProduct_id(@Param("product_id") Long product_id);

    @Query("select new com.petshop.model.dto.response.ProductResponse(p.product_id, p.subCategory.category.category_id, " +
            "p.subCategory.sub_category_id, p.product_name, p.quantity, p.price, p.description, p.image, d.discount_value) " +
            " FROM Product p LEFT JOIN p.discount d")
    List<ProductResponse> getProductAndDiscount();

    @Query("select new com.petshop.model.dto.response.ProductResponse(" +
            "p.product_id, p.subCategory.category.category_id, p.subCategory.sub_category_id, " +
            "p.product_name, p.quantity, p.price, p.description, p.image, d.discount_value) " +
            "from Product p LEFT JOIN p.discount d where p.product_name like %:product_name% and p.price BETWEEN :minPrice AND :maxPrice " +
            "order by case when :sortOrder = 'Desc' then p.price end desc, case when :sortOrder = 'Asc' then p.price end asc ")
    List<ProductResponse> findByName(Pageable pageable, @Param("product_name") String product_name, @Param("sortOrder") String sortOrder,@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);

    @Query("select  new com.petshop.model.dto.response.ProductResponse(p.product_id,p.subCategory.category.category_id,p.subCategory.sub_category_id,p.product_name,p.quantity,p.price,p.description,p.image, d.discount_value) from Product p join p.subCategory s LEFT JOIN p.discount d  where p.subCategory.sub_category_name like %:sub_category_name% and p.price BETWEEN :minPrice AND :maxPrice " +
            "order by case when :sortOrder = 'Desc' then p.price end desc, case when :sortOrder = 'Asc' then p.price end asc ")
    List<ProductResponse> findBySubCategories(Pageable pageable, @Param("sub_category_name") String sub_category_name, @Param("sortOrder") String sortOrder,@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);
}

