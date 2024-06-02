package com.petshop.services.interfaces;

import com.petshop.model.dto.response.ResponseObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ProductService {
    ResponseEntity<ResponseObject> findProductByCategoryID(Integer page , Integer size, Long category_id);
    ResponseEntity<ResponseObject> findRandomProducts(Pageable pageable);
    ResponseEntity<ResponseObject> findAll();
    ResponseEntity<ResponseObject> findTopSaleProduct(Integer page , Integer size);
    ResponseEntity<ResponseObject> findProductBySubcategoryId(Integer page ,Integer size,Long sub_category_id);
    ResponseEntity<ResponseObject> findByProductNameContainingIgnoreCase(Integer page ,Integer size,String name);
    ResponseEntity<ResponseObject> findProductBySubCategoryNameOrProductName(Integer page ,Integer size,String subcategory);
    ResponseEntity<ResponseObject> findProductBySearchFilter(Integer page, Integer size, String sort, Double minPrice, Double maxPrice,Long selected_category_id, String searchValue);
}
