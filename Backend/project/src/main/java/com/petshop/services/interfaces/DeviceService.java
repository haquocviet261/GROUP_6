package com.petshop.services.interfaces;

import com.petshop.model.dto.response.ResponseObject;
import com.petshop.model.entity.Device;
import org.springframework.http.ResponseEntity;

public interface DeviceService {
    ResponseEntity<ResponseObject> getAllDevice();
}
