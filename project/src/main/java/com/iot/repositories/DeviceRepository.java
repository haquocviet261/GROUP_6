package com.iot.repositories;

import com.iot.model.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device,Long> {
    @Query("SELECT d FROM Device d WHERE d.companyId =:company_id")
    List<Device> getDeviceByCompanyId(@Param("company_id") Long company_id);
    @Query("SELECT COUNT(d.id) from Device d where d.companyId = :company_id")
    Integer getTotalDeviceByCompanyId(Long company_id);
}
