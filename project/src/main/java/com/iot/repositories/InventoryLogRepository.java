package com.iot.repositories;

import com.iot.model.entity.InventoryLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface InventoryLogRepository extends JpaRepository<InventoryLog, Long> {
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("DELETE FROM InventoryLog il WHERE il.created_at < :date")
    int deleteInventoryLogsOlderThan(@Param("date") LocalDateTime date);

    @Query("SELECT i FROM InventoryLog i WHERE i.companyId =:companyId " +
            "AND i.created_at BETWEEN :startDate AND :endDate " +
            "AND i.closingQuantity IS NOT NULL")
    List<InventoryLog> getInventoryLogByCompany(@Param("companyId") Long companyId,
                                                @Param("startDate") Date startDate,
                                                @Param("endDate") Date endDate
    );

    @Query("SELECT il FROM InventoryLog il " +
            "WHERE il.foodItemId = :foodItemId " +
            "AND il.foodName = :foodName " +
            "AND il.created_at BETWEEN :startDate AND :endDate " +
            "AND il.closingQuantity IS NOT NULL")
    Optional<InventoryLog> findByFoodItemIdAndCreatedAt(@Param("foodItemId") Long foodItemId,
                                                        @Param("foodName") String foodName,
                                                        @Param("startDate") Date startDate,
                                                        @Param("endDate") Date endDate
    );

    @Query("SELECT il FROM InventoryLog il " +
            "WHERE il.foodItemId = :foodItemId " +
            "AND il.created_at BETWEEN :startDate AND :endDate " +
            "AND il.closingQuantity IS NOT NULL")
    List<InventoryLog> findByFoodItemIdAndCreatedAt(@Param("foodItemId") Long foodItemId,
                                                        @Param("startDate") Date startDate,
                                                        @Param("endDate") Date endDate
    );

}
