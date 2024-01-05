package com.petshop.controller;

import com.petshop.services.imp.ProductServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/product")
public class ProductController {
    @Autowired
    ProductServiceImp productServiceImp;
    @GetMapping("/all")
    public ResponseEntity<?> getAll(){
        return productServiceImp.findAll();
    }

    @GetMapping("/search")
    public ResponseEntity<?> findProductByName(@RequestParam String name){
         return productServiceImp.findByProductNameContainingIgnoreCase(name);
    }

    @GetMapping("/sale")
    public ResponseEntity<?> sale(){
        return productServiceImp.findTopSaleProduct();
    }
    @GetMapping("/random")
    public ResponseEntity<?> random(){
        return productServiceImp.findRandomProducts();
    }

    @GetMapping("/sub_category_id")// sub_category_id?sub_category_id=
    public ResponseEntity<?> findProductBySubCategoryId(@RequestParam Long sub_category_id){

        return productServiceImp.findProductBySubcategoryId(sub_category_id);
    }
    @GetMapping("/find")// find?subcategory=
    public ResponseEntity<?> findProductBySubCategoryNameOrProductName(@RequestParam String subcategory){
        return productServiceImp.findProductBySubCategoryNameOrProductName(subcategory);
    }
    
}
