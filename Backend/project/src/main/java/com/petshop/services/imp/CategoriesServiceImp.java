package com.petshop.services.imp;

import com.petshop.model.dto.response.ResponseObject;
import com.petshop.repositories.CategoriesRepository;
import com.petshop.services.interfaces.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CategoriesServiceImp implements CategoriesService {
    @Autowired
    CategoriesRepository categoriesRepository;
    @Override
    public ResponseEntity<ResponseObject> findAll() {
        return ResponseEntity.ok(new ResponseObject("OK","Get all Categories successfully",categoriesRepository.findAll()));
    }
}
