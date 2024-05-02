package com.petshop.controller;

import com.petshop.models.entities.SubCategory;
import com.petshop.services.imp.SubCategoriesServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subcategory")

public class SubCategoriesController {
    @Autowired
    SubCategoriesServiceImp categoriesServiceImp;
    @Autowired
    SubCategoriesServiceImp subCategoriesServiceImp;
    @GetMapping("/all")
    public ResponseEntity<?> getAll(){
        return categoriesServiceImp.findAll();
    }

    @GetMapping("/{category_id}")
    public ResponseEntity<?> getSubcategoryByCategoryId(@PathVariable Long category_id){
        return categoriesServiceImp.findSubCategoriesByCategoryId(category_id);
    }
    @GetMapping("/all-subcategory")
    public ResponseEntity<?> getAllCategories(){
        return subCategoriesServiceImp.findAll();
    }

    @GetMapping("/find-subcategories")// find-subcategories?category_id=
    public ResponseEntity<?> getSubcategoryByCategoryID(@RequestParam Long category_id){
        return subCategoriesServiceImp.findSubCategoriesByCategoryId(category_id);
    }
}
