package com.petshop.services.imp;

import com.petshop.models.dto.response.ResponseObject;
import com.petshop.models.entities.SubCategory;
import com.petshop.repositories.SubCategoryRepository;
import com.petshop.services.interfaces.SubCategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SubCategoriesServiceImp implements SubCategoriesService {
    @Autowired
    SubCategoryRepository subCategoryRepository;

    @Override
    public ResponseEntity<ResponseObject> findSubCategoriesByCategoryId(Long category_id) {
        return ResponseEntity.ok(new ResponseObject("OK","Get all SubCategories successfully",subCategoryRepository.findSubcategoryByCategoryId(category_id)));
    }

    @Override
    public ResponseEntity<ResponseObject> findAll() {
        return ResponseEntity.ok(new ResponseObject("OK","Get all SubCategories successfully",subCategoryRepository.findAll()));
    }
}
