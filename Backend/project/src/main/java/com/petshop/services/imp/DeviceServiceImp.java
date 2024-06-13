package com.petshop.services.imp;

import com.petshop.model.dto.response.ResponseObject;
import com.petshop.repositories.DeviceRepository;
import com.petshop.services.interfaces.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
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
}
