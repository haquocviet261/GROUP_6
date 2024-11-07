package com.iot.repositories;

import com.iot.model.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DeviceRepository extends JpaRepository<Device,Long> {
    @Query("SELECT d FROM Device d WHERE d.companyId =:company_id")
    Device getDeviceByCompanyId(@Param("company_id") Long company_id);
}
