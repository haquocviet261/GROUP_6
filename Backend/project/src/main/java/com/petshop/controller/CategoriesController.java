package com.petshop.controller;

import com.petshop.services.imp.CategoriesServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/category")
public class CategoriesController {
    @Autowired
    CategoriesServiceImp categoriesServiceImp;
    @GetMapping("/all-category")
    public ResponseEntity<?> getAll(){
        return categoriesServiceImp.findAll();
    }
}
