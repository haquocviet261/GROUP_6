package com.petshop.controller;

import com.petshop.model.dto.response.ResponseObject;
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
    public ResponseEntity<?> findProductByName(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "8") Integer size,@RequestParam String name){
         return productServiceImp.findByProductNameContainingIgnoreCase( page , size,name);
    }

    @GetMapping("/sale")
    public ResponseEntity<?> sale(){
        return productServiceImp.findTopSaleProduct();
    }
    @GetMapping("/random")
    public ResponseEntity<?> random(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "8") Integer size){
        return productServiceImp.findRandomProducts(PageRequest.of(page,size));
    }

    @GetMapping("/sub_category_id")// sub_category_id?sub_category_id=
    public ResponseEntity<?> findProductBySubCategoryId(@RequestParam Long sub_category_id){

        return productServiceImp.findProductBySubcategoryId(sub_category_id);
    }
    @GetMapping("/find")// find?subcategory=
    public ResponseEntity<?> findProductBySubCategoryNameOrProductName(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size,@RequestParam String subcategory){
        return productServiceImp.findProductBySubCategoryNameOrProductName(page,size,subcategory);
    }
    @GetMapping("/category")
    public ResponseEntity<ResponseObject> findProductByCategoryID(@RequestParam Long category_id, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size){
        return productServiceImp.findProductByCategoryID(page,size,category_id);
    }
    @GetMapping("/mobile/find")
    public ResponseEntity<ResponseObject> findProductBySearchOption(@RequestParam(required = false) Integer page,
                                                                    @RequestParam(required = false) Integer size,@RequestParam(required = false) String sort,
                                                                    @RequestParam(required = false) Double min_price,@RequestParam(required = false) Double max_price,
                                                                    @RequestParam String search_value){
        return productServiceImp.findProductBySearchFilter(page,size,sort,min_price,max_price,search_value);
    }
}
