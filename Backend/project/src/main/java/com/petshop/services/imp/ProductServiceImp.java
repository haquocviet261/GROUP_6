package com.petshop.services.imp;

import com.petshop.models.dto.response.DiscountProductResponse;
import com.petshop.models.dto.response.ResponseObject;
import com.petshop.models.entities.Categories;
import com.petshop.models.entities.Product;
import com.petshop.repositories.ProductRepository;
import com.petshop.services.interfaces.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        List<Object[]> products = productRepository.getProductAndDiscount();
        List<DiscountProductResponse> discountProductResponses = new ArrayList<>();
        for (Object[] result: products) {
            Product current_product = (Product) result[0];
            Double discountValue = (Double) result[1];
            discountProductResponses.add(convertProductDiscountResponse(current_product,discountValue));
        }
        return ResponseEntity.ok(new ResponseObject("OK","List all products",discountProductResponses));
    }
    public  ResponseEntity<ResponseObject> findTopSaleProduct(){
        List<Object[]> products = productRepository.findTopSaleProduct();
        List<DiscountProductResponse> discountProductResponses = new ArrayList<>();
        for (Object[] result: products) {
            Product current_product = (Product) result[0];
            Double discountValue = (Double) result[1];
            discountProductResponses.add(convertProductDiscountResponse(current_product,discountValue));
        }
        return ResponseEntity.ok(new ResponseObject("OK","List all products desc",discountProductResponses));

    }

    @Override
    public ResponseEntity<ResponseObject> findProductBySubcategoryId(Long sub_category_id) {
        List<Object[]> products = productRepository.findProductBySubcategoryId(sub_category_id);
        List<DiscountProductResponse> discountProductResponses = new ArrayList<>();
        for (Object[] result: products) {
            Product current_product = (Product) result[0];
            Double discountValue = (Double) result[1];
            discountProductResponses.add(convertProductDiscountResponse(current_product,discountValue));
        }
        return ResponseEntity.ok(new ResponseObject("OK","List all products by sub_category_id",discountProductResponses));
    }

    public ResponseEntity<ResponseObject> findByProductNameContainingIgnoreCase(String name) {
        List<Object[]> productList = productRepository.findByNameContainingIgnoreCase(name);
        if (productList.size()==0){
            return ResponseEntity.ok(new ResponseObject("False","Cannot find product with name: "+name,""));
        }
        return ResponseEntity.ok(new ResponseObject("OK","List name of product",productList));
    }

    public ResponseEntity<ResponseObject> findProductBySubCategoryNameOrProductName(String name) {
        List<Object[]> productListBySubcategories =productRepository.findBySubCategoriesContainingIgnoreCase(name);
        List<Object[]> productListByProductName =productRepository.findByNameContainingIgnoreCase(name);
        List<DiscountProductResponse> discountProductResponses = new ArrayList<>();
        if (productListByProductName.size() !=0){
            for (Object[] result: productListByProductName) {
                Product current_product = (Product) result[0];
                Double discountValue = (Double) result[1];
                discountProductResponses.add(convertProductDiscountResponse(current_product,discountValue));
            }
            return ResponseEntity.ok(new ResponseObject("OK","List of product by Name: "+name,discountProductResponses));

        } else if (productListBySubcategories.size() !=0) {
            for (Object[] result: productListBySubcategories) {
                Product current_product = (Product) result[0];
                Double discountValue = (Double) result[1];
                discountProductResponses.add(convertProductDiscountResponse(current_product,discountValue));
            }
            return ResponseEntity.ok(new ResponseObject("OK","List of  by Subcategory: "+name,discountProductResponses));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseObject("False","Cannot find product with name: "+name,""));

    }
    private DiscountProductResponse convertProductDiscountResponse(Product current_product,Double discount){
            DiscountProductResponse discountProductResponse = new DiscountProductResponse();
            if( discount == null){
                discount = Double.valueOf(0);
            }
            discountProductResponse.setProduct_id(current_product.getProduct_id());
            discountProductResponse.setProduct_name(current_product.getProduct_name());
            discountProductResponse.setDiscount(discount);
            discountProductResponse.setPrice(current_product.getPrice());
            discountProductResponse.setImage(current_product.getImage());
            discountProductResponse.setQuantity(current_product.getQuantity());
            discountProductResponse.setSub_category_id(current_product.getSubCategory().getSub_category_id());
            discountProductResponse.setDescription(current_product.getDescription());
            return discountProductResponse;
    }
}
