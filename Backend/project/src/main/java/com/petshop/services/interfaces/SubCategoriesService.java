package com.petshop.services.interfaces;

import com.petshop.models.dto.response.ResponseObject;
import com.petshop.models.entities.SubCategory;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SubCategoriesService {
    public ResponseEntity<ResponseObject> findSubCategoriesByCategoryId(Long category_id);
    public ResponseEntity<ResponseObject> findAll();
}
