package com.iot.repositories;

import com.iot.model.entity.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {
    @Query("SELECT fi FROM FoodItem fi WHERE fi.deleted_at IS NULL")
    List<FoodItem> getAllFoodItem();

    @Query("SELECT fi FROM FoodItem fi WHERE fi.id = :id AND fi.deleted_at IS NULL")
    Optional<FoodItem> findById(@Param("id") Integer foodItem_id);

    @Query("SELECT fi FROM FoodItem fi WHERE fi.name LIKE %:keyword% AND fi.deleted_at IS NULL")
    List<FoodItem> searchFoodItemByName(@Param("keyword") String keyword);
}
