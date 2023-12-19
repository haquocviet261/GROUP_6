package com.petshop.controller;

import com.petshop.services.imp.ProductServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/product")
public class ProductController {
    @Autowired
    ProductServiceImp productServiceImp;
    @GetMapping("/all")
    public ResponseEntity<?> getAll(){
        return productServiceImp.findAll();
    }
    @GetMapping("/random")
    public ResponseEntity<?> random(){
        return productServiceImp.findRandomProducts();
    }

    @GetMapping("/search")
    public ResponseEntity<?> findProductByName(@RequestParam String name){
         return productServiceImp.findByProductNameContainingIgnoreCase(name);
    }
    @GetMapping("/find")
    public ResponseEntity<?> findProductBySubCategoryName(@RequestParam String subcategory){
        return productServiceImp.findProductBySubCategoryNameOrProductName(subcategory);
    }

    
}
