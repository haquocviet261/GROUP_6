package com.iot.repositories;

import com.iot.model.entity.TemperatureHumidity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface TemperatureHumidityRepository extends JpaRepository<TemperatureHumidity, Long> {
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("DELETE FROM TemperatureHumidity th WHERE th.created_at < :date")
    int deleteTemperatureHumidityOlderThan(@Param("date") LocalDateTime date);

    @Query("SELECT th FROM TemperatureHumidity th WHERE th.companyId =:company_id")
    List<TemperatureHumidity> getAllTemperatureHumidityByCompanyId(@Param("company_id") Long company_id);

    @Query("SELECT th FROM TemperatureHumidity th WHERE th.companyId = :companyId ORDER BY th.created_at DESC")
    List<TemperatureHumidity> getAllByCompanyIdAndCreatedAtDesc(@Param("companyId") Long companyId);

    @Query("SELECT th FROM TemperatureHumidity th WHERE th.companyId = :companyId ORDER BY th.created_at ASC")
    List<TemperatureHumidity> getAllByCompanyIdAndCreatedAtAsc(@Param("companyId") Long companyId);

    @Query("SELECT th FROM TemperatureHumidity th WHERE th.companyId = :companyId AND th.created_at BETWEEN :startDate AND :endDate ORDER BY th.created_at ASC")
    List<TemperatureHumidity> getAllByCompanyIdAndDateRange(
            @Param("companyId") Long companyId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );

}
