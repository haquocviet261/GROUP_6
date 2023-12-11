package com.petshop.services.imp;

import com.petshop.models.dto.response.ResponseObject;
import com.petshop.models.entities.Categories;
import com.petshop.models.entities.Product;
import com.petshop.repositories.ProductRepository;
import com.petshop.services.interfaces.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductServiceImp implements ProductService {
    @Autowired
    ProductRepository productRepository;
    @Override
    public ResponseEntity<ResponseObject> findRandomProducts() {
        return ResponseEntity.ok(new ResponseObject("OK","List random top 30 product",productRepository.findRandomProducts()));
    }

    @Override
    public ResponseEntity<ResponseObject> findAll() {

        return ResponseEntity.ok(new ResponseObject("OK","List all products",productRepository.findAll()));
    }
    public  ResponseEntity<ResponseObject> findTopSaleProduct(){
        return ResponseEntity.ok(new ResponseObject("OK","List all products desc",productRepository.findTopSaleProduct()));

    }

    @Override
    public ResponseEntity<ResponseObject> findProductBySubcategoryId(Long sub_category_id) {
        return ResponseEntity.ok(new ResponseObject("OK","List all products by sub_category_id",productRepository.findProductBySubcategoryId(sub_category_id)));
    }
}
