package com.iot.repositories;

import com.iot.model.entity.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {
    @Query("SELECT fi FROM FoodItem fi WHERE fi.deleted_at IS NULL AND fi.companyId = :company_id AND fi.quantity IS NOT NULL")
    List<FoodItem> getAllFoodItem(@Param("company_id") Integer company_id);

    @Query("SELECT fi FROM FoodItem fi WHERE fi.id = :id AND fi.deleted_at IS NULL AND fi.quantity IS NOT NULL")
    Optional<FoodItem> getFoodItemById(@Param("id") Integer foodItem_id);

    @Query("SELECT fi FROM FoodItem fi WHERE fi.name LIKE %:keyword% AND fi.deleted_at IS NULL AND fi.companyId = :company_id AND fi.quantity IS NOT NULL")
    List<FoodItem> searchFoodItemByName(@Param("keyword") String keyword,@Param("company_id") Integer company_id);

    @Query("SELECT fi FROM FoodItem fi " +
            "WHERE fi.categoryId = :category_id AND fi.deleted_at IS NULL AND fi.companyId = :company_id AND fi.quantity IS NOT NULL")
    List<FoodItem> getListFoodItemByCategory(@Param("category_id") Integer category_id,@Param("company_id") Integer company_id);

    @Query(value = "SELECT * FROM FoodItem f WHERE f.company_id = :company_id AND f.quantity IS NOT NULL", nativeQuery = true)
    List<FoodItem> findByCompanyId(@Param("company_id") Long company_id);
}
