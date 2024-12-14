package com.iot.controller;

import com.iot.model.dto.request.DeviceRequest;
import com.iot.model.dto.response.ResponseObject;
import com.iot.model.entity.Device;
import com.iot.repositories.DeviceRepository;
import com.iot.services.imp.DeviceServiceImp;
import com.iot.services.interfaces.DeviceService;
import org.json.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/device")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private DeviceServiceImp deviceServiceImp;
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/save")
    public ResponseEntity<ResponseObject> saveDevice(@RequestBody DeviceRequest deviceRequest) {
        return deviceServiceImp.saveDevice(deviceRequest);
    }
    @GetMapping("/get")
    public ResponseObject getDeviceByCompanyId(@RequestParam("company_id") Long conpany_id) {
        return new ResponseObject(HttpStatus.OK.toString(), "Get device successfully", deviceService.getDeviceByCompanyId(conpany_id));
    }

    @DeleteMapping ("/delete")
    public ResponseObject deleteDeviceAndFoodItems(@RequestParam("device_id") Long device_id) {
        return new ResponseObject(HttpStatus.OK.toString(), " Delete device successfully", deviceService.deleteDeviceAndFoodItems(device_id));
    }

}
