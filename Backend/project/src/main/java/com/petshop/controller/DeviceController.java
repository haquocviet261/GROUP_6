package com.petshop.controller;

import com.petshop.model.dto.response.ResponseObject;
import com.petshop.services.imp.DeviceServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/device")
public class DeviceController {
    @Autowired
    private DeviceServiceImp deviceServiceImp;

    @GetMapping("/all")
    public ResponseEntity<ResponseObject> getDeviceByUserID(){
        return deviceServiceImp.getAllDevice();
    }
}
