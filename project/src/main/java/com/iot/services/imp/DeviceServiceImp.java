package com.iot.services.imp;

import com.iot.model.dto.response.ResponseObject;
import com.iot.repositories.DeviceRepository;
import com.iot.services.interfaces.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DeviceServiceImp implements DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;
    @Override
    public ResponseEntity<ResponseObject> getAllDevice() {
        return ResponseEntity.ok(new ResponseObject("OK","List Device",deviceRepository.findAll()));
    }

    @Override
    public ResponseEntity<ResponseObject> getDeviceByCompanyId(Long company_id) {
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.toString(), "Get device successfully!", deviceRepository.getDeviceByCompanyId(company_id)));
    }

}
