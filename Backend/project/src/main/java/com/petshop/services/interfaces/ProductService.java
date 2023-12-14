package com.petshop.services.interfaces;

import com.petshop.models.dto.response.ResponseObject;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    ResponseEntity<ResponseObject> findRandomProducts();
    ResponseEntity<ResponseObject> findAll();
    ResponseEntity<ResponseObject> findTopSaleProduct();
    ResponseEntity<ResponseObject> findProductBySubcategoryId(Long sub_category_id);
    ResponseEntity<ResponseObject> findByProductNameContainingIgnoreCase(String name);
    ResponseEntity<ResponseObject> findProductBySubCategoryNameOrProductName(String subcategory);
}
