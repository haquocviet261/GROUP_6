package com.iot.repositories;

import com.iot.model.entity.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

public interface FoodItemRepository extends JpaRepository<FoodItem,Long> {
    @Query(value = "SELECT * FROM FoodItem f WHERE f.company_id = :company_id", nativeQuery = true)
    List<FoodItem> findByCompanyId(@Param("company_id") Long company_id);
}
