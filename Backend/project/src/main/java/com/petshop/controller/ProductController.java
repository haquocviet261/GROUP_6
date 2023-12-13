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
    @GetMapping("/sub_category_id")
    public ResponseEntity<?> findProductBySubCategoryId(@RequestParam Long sub_category_id){
        return productServiceImp.findProductBySubcategoryId(sub_category_id);
    }
}
