package com.petshop.services.interfaces;

import com.petshop.model.dto.response.ResponseObject;
import org.springframework.http.ResponseEntity;

public interface CategoriesService {
    public ResponseEntity<ResponseObject> findAll();
}
