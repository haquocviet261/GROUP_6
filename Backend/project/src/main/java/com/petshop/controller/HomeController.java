package com.petshop.controller;

import com.petshop.services.imp.CategoriesServiceImp;
import com.petshop.services.imp.ProductServiceImp;
import com.petshop.services.imp.SubCategoriesServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/home")
public class HomeController {
    @Autowired
    ProductServiceImp productServiceImp;
    @Autowired
    CategoriesServiceImp categoriesServiceImp;
    @Autowired
    SubCategoriesServiceImp subCategoriesServiceImp;
    @GetMapping("/find-subcategories")
    public ResponseEntity<?> getSubcategoryByCategoryId(@RequestParam Long category_id){
        return subCategoriesServiceImp.findSubCategoriesByCategoryId(category_id);
    }
    @GetMapping("/all-subcategory")
    public ResponseEntity<?> getAllCategories(){
        return subCategoriesServiceImp.findAll();
    }

    @GetMapping("/find")
    public ResponseEntity<?> findProductBySubCategoryNameOrProductName(@RequestParam String subcategory){
        return productServiceImp.findProductBySubCategoryNameOrProductName(subcategory);
    }
    @GetMapping("/all-category")
    public ResponseEntity<?> getAll(){
        return categoriesServiceImp.findAll();
    }

    @GetMapping("/all-product")
    public ResponseEntity<?> getAllProduct(){
        return productServiceImp.findAll();
    }
    @GetMapping("/random")
    public ResponseEntity<?> random(){
        return productServiceImp.findRandomProducts();
    }

    @GetMapping("/sale")
    public ResponseEntity<?> sale(){
        return productServiceImp.findTopSaleProduct();
    }

}
