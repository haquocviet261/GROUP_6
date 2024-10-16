package com.iot.repositories;

import com.iot.model.dto.response.FoodResponse;
import com.iot.model.entity.Food;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FoodRepository extends JpaRepository<Food,Long> {
    @Query("SELECT new com.iot.model.dto.response.FoodResponse(f.name, f.expired_date, f.category_id)" +
            " FROM Food f WHERE f.name = :name AND f.deleted_at IS NULL")
    Optional<FoodResponse> findFoodByName(@Param("name") String name);

    @Query("SELECT new com.iot.model.dto.response.FoodResponse(f.name, f.expired_date, f.category_id)" +
            " FROM Food f WHERE f.category_id = :category_id AND f.deleted_at IS NULL")
    List<FoodResponse> findFoodByCategoryId(@Param("category_id") Integer category_id);

    @Query("SELECT new com.iot.model.dto.response.FoodResponse(f.name, f.expired_date, f.category_id)" +
            " FROM Food f WHERE f.deleted_at IS NULL")
    List<FoodResponse> getAllFood();

    @Query("SELECT f FROM Food f WHERE f.id = :id AND f.deleted_at IS NULL")
    Optional<Food> findById(@Param("id") Integer food_id);

}
