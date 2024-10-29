package com.iot.services.interfaces;

import com.iot.model.dto.response.ResponseObject;
import org.springframework.http.ResponseEntity;

public interface CompanyService {
    ResponseEntity<ResponseObject> getAllNameCompany();
}
