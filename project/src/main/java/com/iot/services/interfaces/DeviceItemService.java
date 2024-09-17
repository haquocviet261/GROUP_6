package com.iot.services.interfaces;

import com.iot.model.dto.request.DeviceItemRequest;
import com.iot.model.dto.response.ResponseObject;
import org.springframework.http.ResponseEntity;

public interface DeviceItemService {
    ResponseEntity<ResponseObject> saveDeviceItem(DeviceItemRequest deviceItemRequest);
    ResponseEntity<ResponseObject> getAllListDeviceItemByUserID(Long user_id);
}
