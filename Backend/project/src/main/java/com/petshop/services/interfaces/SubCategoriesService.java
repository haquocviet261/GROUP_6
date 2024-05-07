package com.petshop.services.interfaces;

import com.petshop.model.dto.response.ResponseObject;
import org.springframework.http.ResponseEntity;

public interface SubCategoriesService {
    public ResponseEntity<ResponseObject> findSubCategoriesByCategoryId(Long category_id);
    public ResponseEntity<ResponseObject> findAll();
}
