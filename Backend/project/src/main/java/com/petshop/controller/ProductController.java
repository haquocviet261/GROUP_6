package com.petshop.controller;

import com.petshop.models.dto.response.ResponseObject;
import com.petshop.services.imp.ProductServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
    public ResponseEntity<?> findProductByName(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "8") int size,@RequestParam String name){
         return productServiceImp.findByProductNameContainingIgnoreCase(PageRequest.of(page,size),name);
    }

    @GetMapping("/sale")
    public ResponseEntity<?> sale(){
        return productServiceImp.findTopSaleProduct();
    }
    @GetMapping("/random")
    public ResponseEntity<?> random(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "8") int size){
        return productServiceImp.findRandomProducts(PageRequest.of(page,size));
    }

    @GetMapping("/sub_category_id")// sub_category_id?sub_category_id=
    public ResponseEntity<?> findProductBySubCategoryId(@RequestParam Long sub_category_id){

        return productServiceImp.findProductBySubcategoryId(sub_category_id);
    }
    @GetMapping("/find")// find?subcategory=
    public ResponseEntity<?> findProductBySubCategoryNameOrProductName(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "8") int size,@RequestParam String subcategory){
        return productServiceImp.findProductBySubCategoryNameOrProductName(PageRequest.of(page,size),subcategory);
    }
    @GetMapping("/category")
    public ResponseEntity<ResponseObject > findProductByCategoryID(@RequestParam Long category_id,@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "8") int size){
        return productServiceImp.findProductByCategoryID(PageRequest.of(page,size),category_id);
    }
}
