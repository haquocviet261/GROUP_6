package com.petshop.controller;

import com.petshop.models.dto.request.CartItemDTO;
import com.petshop.models.entities.Cart;
import com.petshop.models.entities.Item;
import com.petshop.services.imp.CategoriesServiceImp;
import com.petshop.services.imp.ProductServiceImp;
import com.petshop.services.imp.ShoppingCartServiceImp;
import com.petshop.services.imp.SubCategoriesServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/v1/home")
public class HomeController {
    @Autowired
    ProductServiceImp productServiceImp;
    @Autowired
    CategoriesServiceImp categoriesServiceImp;
    @Autowired
    SubCategoriesServiceImp subCategoriesServiceImp;
    @Autowired
    ShoppingCartServiceImp shoppingCartServiceImp;
    @GetMapping("/find-subcategories")// find-subcategories?category_id=
    public ResponseEntity<?> getSubcategoryByCategoryId(@RequestParam Long category_id){
        return subCategoriesServiceImp.findSubCategoriesByCategoryId(category_id);
    }

    @GetMapping("/sub_category_id")// sub_category_id?sub_category_id=
    public ResponseEntity<?> findProductBySubCategoryId(@RequestParam Long sub_category_id){
        return productServiceImp.findProductBySubcategoryId(sub_category_id);
    }

    @GetMapping("/all-subcategory")
    public ResponseEntity<?> getAllCategories(){
        return subCategoriesServiceImp.findAll();
    }

    @GetMapping("/find")// find?subcategory=
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

    @PostMapping("/cart")
    public ResponseEntity<?> addToCart(@RequestBody List<CartItemDTO> items, Principal user){
        return ResponseEntity.ok(shoppingCartServiceImp.addToCart(items,user));
    }
    @GetMapping("/get-cart")
    public ResponseEntity<?> getCartByUserId(Principal user){
        return ResponseEntity.ok(shoppingCartServiceImp.getShoppingCartByUserId(user));
    }
    @DeleteMapping("/item")
    public ResponseEntity<?> removeItemFromCart(@RequestParam Long item_id, Principal user) {
        return ResponseEntity.ok(shoppingCartServiceImp.removeItemFromCart(item_id,user));
    }

}
