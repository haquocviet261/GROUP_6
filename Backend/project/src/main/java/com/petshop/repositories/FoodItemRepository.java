package com.petshop.repositories;

import com.petshop.model.entity.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FoodItemRepository extends JpaRepository<FoodItem,Long> {
    @Query("select f from FoodItem f where f.deviceItem.device_item_id = : device_item_id")
    List<FoodItem> getFoodItemByDeviceItemID(@Param("device_item_id") Long device_item_id);
}
