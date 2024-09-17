package com.iot.services.imp;

import com.iot.model.dto.request.DeviceItemRequest;
import com.iot.model.dto.response.ResponseObject;
import com.iot.model.entity.Device;
import com.iot.model.entity.DeviceItem;
import com.iot.model.entity.User;
import com.iot.repositories.DeviceItemRepository;
import com.iot.repositories.DeviceRepository;
import com.iot.repositories.UserRepository;
import com.iot.services.interfaces.DeviceItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DeviceItemServiceImp implements DeviceItemService {
    @Autowired
    private DeviceItemRepository deviceItemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    UserServiceImp userServiceImp;
    @Autowired
    private DeviceRepository deviceRepository;
    @Override
    public ResponseEntity<ResponseObject> saveDeviceItem(DeviceItemRequest deviceItemRequest) {
        User user = userRepository.findById(deviceItemRequest.getUser_id()).get();
        Device device = deviceRepository.findById(deviceItemRequest.getDevice_id()).get();
        DeviceItem deviceItem = new DeviceItem(deviceItemRequest.getDevice_name(),deviceItemRequest.getMac_address(),device,user);
        return ResponseEntity.ok(new ResponseObject("OK","Add Device successfully",deviceItemRepository.save(deviceItem)));
    }

    @Override
    public ResponseEntity<ResponseObject> getAllListDeviceItemByUserID(Long user_id) {
        return ResponseEntity.ok(new ResponseObject("OK","get Device Successfully",deviceItemRepository.getDeviceByUserID(user_id)));
    }
}
