package com.iot.repositories;


import com.iot.model.dto.response.FoodCategoryResponse;
import com.iot.model.entity.FoodCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FoodCategoryRepository extends JpaRepository<FoodCategory,Long> {
    @Query("SELECT new com.iot.model.dto.response.FoodCategoryResponse(fc.category_name) fc FROM FoodCategory fc WHERE fc.deleted_at IS NULL")
    List<FoodCategoryResponse> getAllFoodCategory();
}
