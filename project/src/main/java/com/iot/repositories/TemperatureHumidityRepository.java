package com.iot.repositories;

import com.iot.model.entity.TemperatureHumidity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TemperatureHumidityRepository extends JpaRepository<TemperatureHumidity, Long> {
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("DELETE FROM TemperatureHumidity di ")
    void deledeBydeviceId();
    @Query("SELECT th FROM TemperatureHumidity th WHERE th.companyId =:company_id")
    List<TemperatureHumidity> getAllTemperatureHumidityByCompanyId(@Param("company_id") Long company_id);

    @Query("SELECT th FROM TemperatureHumidity th WHERE th.companyId = :companyId ORDER BY th.created_at DESC")
    List<TemperatureHumidity> getAllByCompanyIdAndCreatedAtDesc(@Param("companyId") Long companyId);
}
