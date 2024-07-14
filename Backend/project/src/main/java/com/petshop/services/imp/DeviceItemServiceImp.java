package com.petshop.services.imp;

import com.petshop.model.dto.request.DeviceItemRequest;
import com.petshop.model.dto.response.ResponseObject;
import com.petshop.model.entity.Device;
import com.petshop.model.entity.DeviceItem;
import com.petshop.model.entity.User;
import com.petshop.repositories.DeviceItemRepository;
import com.petshop.repositories.DeviceRepository;
import com.petshop.repositories.UserRepository;
import com.petshop.services.interfaces.DeviceItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
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
