package com.iot.controller;

import com.iot.model.dto.request.DeviceItemRequest;
import com.iot.model.dto.response.ResponseObject;
import com.iot.services.imp.DeviceItemServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/device/item")
public class DeviceItemController {
    @Autowired
    private DeviceItemServiceImp deviceItemServiceImp;
    @PostMapping("/add")
    public ResponseEntity<ResponseObject> addDevice(@RequestBody DeviceItemRequest deviceItemRequest){
        return deviceItemServiceImp.saveDeviceItem(deviceItemRequest);
    }
    @GetMapping("/find")
    public ResponseEntity<ResponseObject> getDeviceItemByUserID(@RequestParam Long user_id){
        return deviceItemServiceImp.getAllListDeviceItemByUserID(user_id);
    }

}
