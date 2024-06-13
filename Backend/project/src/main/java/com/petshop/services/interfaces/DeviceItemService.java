package com.petshop.services.interfaces;

import com.petshop.model.dto.request.DeviceItemRequest;
import com.petshop.model.dto.response.ResponseObject;
import org.springframework.http.ResponseEntity;

public interface DeviceItemService {
    ResponseEntity<ResponseObject> saveDeviceItem(DeviceItemRequest deviceItemRequest);
    ResponseEntity<ResponseObject> getAllListDeviceItemByUserID(Long user_id);
}
