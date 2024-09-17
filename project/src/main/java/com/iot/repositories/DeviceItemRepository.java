package com.iot.repositories;

import com.iot.model.dto.response.DeviceItemResponse;
import com.iot.model.entity.DeviceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DeviceItemRepository extends JpaRepository<DeviceItem,Long> {
    @Query("select new com.petshop.model.dto.response.DeviceItemResponse(di.device_item_id,di.device_name,di.mac_address,di.device.device_id,di.user.user_id) from DeviceItem di where di.user.user_id = :user_id")
    List<DeviceItemResponse> getDeviceByUserID(@Param("user_id")Long user_id);
}
