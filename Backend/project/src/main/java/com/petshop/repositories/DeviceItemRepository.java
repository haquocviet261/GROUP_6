package com.petshop.repositories;

import com.petshop.model.entity.DeviceItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceItemRepository extends JpaRepository<DeviceItem,Long> {
}
