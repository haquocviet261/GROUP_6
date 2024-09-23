package com.iot.repositories;

import com.iot.model.entity.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface FoodItemRepository extends JpaRepository<FoodItem,Long> {

}
