package com.petshop.services.imp;

import com.petshop.models.dto.response.DiscountProductResponse;
import com.petshop.models.dto.response.ResponseObject;
import com.petshop.models.entities.Categories;
import com.petshop.models.entities.Product;
import com.petshop.repositories.ProductRepository;
import com.petshop.services.interfaces.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<ResponseObject> findProductByCategoryID(Pageable pageable,Long category_id) {
        List<Object[]> products = productRepository.findProductByCategory(pageable,category_id);
        List<DiscountProductResponse> discountProductResponses = new ArrayList<>();
        for (int i = 0; i < products.size() ; i++) {
            Product current_product = (Product) products.get(i)[0];
            Double discountValue = (Double) products.get(i)[1];
            Long current_category_id = (Long) products.get(i)[2];
            discountProductResponses.add(convertProductDiscountResponse(current_product,discountValue,current_category_id));
        }
        return ResponseEntity.ok(new ResponseObject("OK","List all products",discountProductResponses));
    }

    @Override
    public ResponseEntity<ResponseObject> findRandomProducts(Pageable pageable) {
        List<Object[]> products = productRepository.findRandomProducts(pageable);
        List<DiscountProductResponse> products_new = new ArrayList<>();
        for (int i = 0; i < products.size() ; i++) {
           DiscountProductResponse discountProductResponse = fromObjectArray(products.get(i));
           products_new.add(discountProductResponse);
        }
        return ResponseEntity.ok(new ResponseObject("OK","List random top product by paging",products_new));
    }

    @Override
    public ResponseEntity<ResponseObject> findAll() {
        List<Object[]> products = productRepository.getProductAndDiscount();
        List<DiscountProductResponse> discountProductResponses = new ArrayList<>();
        for (int i = 0; i < products.size() ; i++) {
            Product current_product = (Product) products.get(i)[0];
            Double discountValue = (Double) products.get(i)[1];
            Long category_id = (Long) products.get(i)[2];
            discountProductResponses.add(convertProductDiscountResponse(current_product,discountValue,category_id));
        }
        return ResponseEntity.ok(new ResponseObject("OK","List all products",discountProductResponses));
    }
    public  ResponseEntity<ResponseObject> findTopSaleProduct(){
        List<Object[]> products = productRepository.findTopSaleProduct();
        List<DiscountProductResponse> discountProductResponses = new ArrayList<>();
        int count =0;
        for (Object[] result: products) {
            Product current_product = (Product) result[0];
            Double discountValue = (Double) result[1];
            Long category_id = (Long) result[2];
            discountProductResponses.add(convertProductDiscountResponse(current_product,discountValue,category_id));
            if (count == 8){
                break;
            }
            count++;
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
            Long category_id = (Long) result[2];
            discountProductResponses.add(convertProductDiscountResponse(current_product,discountValue,category_id));
        }
        return ResponseEntity.ok(new ResponseObject("OK","List all products by sub_category_id",discountProductResponses));
    }

    public ResponseEntity<ResponseObject> findByProductNameContainingIgnoreCase(Pageable pageable,String name) {
        List<Object[]> productList = new ArrayList<>();
        if (pageable == null){
            productList = productRepository.findByNameContainingIgnoreCase(name);
        }else {
            productList = productRepository.findByNameContainingIgnoreCase(pageable,name);
        }

        if (productList.size()==0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("False","Cannot find product with name: "+name,""));
        }
        return ResponseEntity.ok(new ResponseObject("OK","List name of product",productList));
    }

    public ResponseEntity<ResponseObject> findProductBySubCategoryNameOrProductName(Pageable pageable,String name) {
        List<Object[]> productListBySubcategories = new ArrayList<>();
        if (pageable == null){
            productListBySubcategories = productRepository.findByNameContainingIgnoreCase(name);
        }else {
            productListBySubcategories = productRepository.findByNameContainingIgnoreCase(pageable,name);
        }
        List<Object[]> productListByProductName =productRepository.findByNameContainingIgnoreCase(pageable,name);
        List<DiscountProductResponse> discountProductResponses = new ArrayList<>();
        if (productListByProductName.size() !=0){
            for (Object[] result: productListByProductName) {
                Product current_product = (Product) result[0];
                Double discountValue = (Double) result[1];
                Long current_category_id = (Long) result[2];
                discountProductResponses.add(convertProductDiscountResponse(current_product,discountValue,current_category_id));
            }
            return ResponseEntity.ok(new ResponseObject("OK","List of product by Name: "+name,discountProductResponses));

        } else if (productListBySubcategories.size() !=0) {
            for (Object[] result: productListBySubcategories) {
                Product current_product = (Product) result[0];
                Double discountValue = (Double) result[1];
                Long category_id = (Long) result[2];
                discountProductResponses.add(convertProductDiscountResponse(current_product,discountValue,category_id));
            }
            return ResponseEntity.ok(new ResponseObject("OK","List of  by Subcategory: "+name,discountProductResponses));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseObject("False","Cannot find product with name: "+name,""));

    }
    private DiscountProductResponse convertProductDiscountResponse(Product current_product,Double discount,Long category_id){
            DiscountProductResponse discountProductResponse = new DiscountProductResponse();
            if( discount == null){
                discount = Double.valueOf(0);
            }
            discountProductResponse.setProduct_id(current_product.getProduct_id());
            discountProductResponse.setProduct_name(current_product.getProduct_name());
            discountProductResponse.setDiscount_value(discount);
            discountProductResponse.setPrice(current_product.getPrice());
            discountProductResponse.setProduct_image(current_product.getImage());
            discountProductResponse.setQuantity(current_product.getQuantity());
            discountProductResponse.setSub_category_id(current_product.getSubCategory().getSub_category_id());
            discountProductResponse.setDescription(current_product.getDescription());
            discountProductResponse.setCategory_id(category_id);
            return discountProductResponse;
    }
    private static DiscountProductResponse fromObjectArray(Object[] row) {
        Long productId = (Long) row[0];
        Long subCategoryId = (Long) row[1];
        String productName = (String) row[2];
        int quantity = (int) row[3];
        double price = (double) row[4];
        String description = (String) row[5];
        String productImage = (String) row[6];
        double discountValue = (row[7] != null) ? (double) row[7] : 0.0;
        Long category_id =(Long) row[8];
        return new DiscountProductResponse(productId,category_id, subCategoryId, productName, quantity, price, description, productImage, discountValue);
    }
}
