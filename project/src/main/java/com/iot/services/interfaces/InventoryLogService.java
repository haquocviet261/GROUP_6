package com.iot.services.interfaces;

import com.iot.model.dto.response.ResponseObject;
import org.springframework.http.ResponseEntity;

import java.util.Date;

public interface InventoryLogService {
    ResponseEntity<ResponseObject> getAllInventoryLog(Long companyId, Date specificDate);
    ResponseEntity<ResponseObject> getConsumedQuantity(Long companyId, Date specificDate);
}
