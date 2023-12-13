package com.petshop.controller;

import com.petshop.models.entities.SubCategory;
import com.petshop.services.imp.SubCategoriesServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/subcategory")

public class SubCategoriesController {
    @Autowired
    SubCategoriesServiceImp categoriesServiceImp;
    @GetMapping("/all")
    public ResponseEntity<?> getAll(){
        return categoriesServiceImp.findAll();
    }

    @GetMapping("/{category_id}")
    public ResponseEntity<?> getSubcategoryByCategoryId(@PathVariable Long category_id){
        return categoriesServiceImp.findSubCategoriesByCategoryId(category_id);
    }
}
