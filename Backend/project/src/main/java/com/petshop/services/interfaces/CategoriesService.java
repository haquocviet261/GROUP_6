package com.petshop.services.interfaces;

import com.petshop.models.dto.response.ResponseObject;
import com.petshop.models.entities.Categories;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoriesService {
    public ResponseEntity<ResponseObject> findAll();
}
