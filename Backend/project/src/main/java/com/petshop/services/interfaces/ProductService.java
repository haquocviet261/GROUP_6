package com.petshop.services.interfaces;

import com.petshop.models.dto.response.ResponseObject;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    ResponseEntity<ResponseObject> findProductByCategoryID(Pageable pageable,Long category_id);
    ResponseEntity<ResponseObject> findRandomProducts(Pageable pageable);
    ResponseEntity<ResponseObject> findAll();
    ResponseEntity<ResponseObject> findTopSaleProduct();
    ResponseEntity<ResponseObject> findProductBySubcategoryId(Long sub_category_id);
    ResponseEntity<ResponseObject> findByProductNameContainingIgnoreCase(Pageable pageable,String name);
    ResponseEntity<ResponseObject> findProductBySubCategoryNameOrProductName(Pageable pageable,String subcategory);
}
