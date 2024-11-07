package com.iot.services.interfaces;

import com.iot.model.dto.response.ResponseObject;
import org.springframework.http.ResponseEntity;

public interface DeviceService {
    ResponseEntity<ResponseObject> getAllDevice();
    ResponseEntity<ResponseObject> getDeviceByCompanyId(Long company_id);
}
