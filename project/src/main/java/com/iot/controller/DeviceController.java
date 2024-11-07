package com.iot.controller;

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
    private DeviceRepository deviceRepository;
    @Autowired
    private DeviceService deviceService;
    @Transactional(rollbackFor = Exception.class)
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseObject saveDevice(@RequestBody Device device) {
        return new ResponseObject(HttpStatus.OK.toString(), "Add device successfully !",deviceRepository.save(device));
    }
    @GetMapping("/get")
    public ResponseObject getDeviceByConpanyId(@RequestParam("company_id") Long conpany_id) {
        return new ResponseObject(HttpStatus.OK.toString(), "Get device successfully", deviceService.getDeviceByCompanyId(conpany_id));
    }
}
