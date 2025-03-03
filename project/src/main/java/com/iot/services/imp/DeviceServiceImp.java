package com.iot.services.imp;

import com.iot.model.dto.request.DeviceRequest;
import com.iot.model.dto.response.DeviceResponse;
import com.iot.model.dto.response.ResponseObject;
import com.iot.model.entity.Device;
import com.iot.model.entity.FoodItem;
import com.iot.repositories.DeviceRepository;
import com.iot.repositories.FoodItemRepository;
import com.iot.services.interfaces.DeviceService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class DeviceServiceImp implements DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private FoodItemRepository foodItemRepository;
    @Override
    public ResponseEntity<ResponseObject> getAllDevice() {
        return ResponseEntity.ok(new ResponseObject("OK","List Device",deviceRepository.findAll()));
    }

    @Override
    public ResponseEntity<ResponseObject> getDeviceByCompanyId(Long company_id) {
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.toString(), "Get device successfully!", deviceRepository.getDeviceByCompanyId(company_id)));
    }

    @Override
    public ResponseEntity<ResponseObject> deleteDeviceAndFoodItems(Long device_id) {
        deviceRepository.deleteById(device_id);
        foodItemRepository.deleteFoodItemsByDeviceId(device_id);
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.toString(), "Delete device successfully!", null));
    }

    public ResponseEntity<ResponseObject> saveDevice(DeviceRequest deviceRequest) {
        List<Device> deviceList = deviceRepository.getDeviceByCompanyId(deviceRequest.getCompanyId());
        boolean isExistDevice = false;
        for (Device device : deviceList) {
            if (device.getMacAddress().equals(deviceRequest.getMacAddress())) {
                isExistDevice = true;
                break;
            }
        }
        if (isExistDevice) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObject(HttpStatus.INTERNAL_SERVER_ERROR.toString(),"Device are exist !", null));
        }
        Device device = new Device();
        BeanUtils.copyProperties(deviceRequest, device);
        device = deviceRepository.save(device);
        Device finalDevice = device;
        List<Long> listFoodItemsId = IntStream.range(0, 10)
                .mapToObj(i -> {
                    // Tạo FoodItem nhưng chưa gán tên
                    FoodItem foodItem = FoodItem.builder()
                            .companyId(deviceRequest.getCompanyId().intValue())
                            .name("Food") // Đặt tạm tên, sẽ sửa sau
                            .deviceId(finalDevice.getId().intValue())
                            .build();

                    FoodItem savedFoodItem = foodItemRepository.save(foodItem);

                    savedFoodItem.setName("Food " + savedFoodItem.getId());

                    foodItemRepository.save(savedFoodItem);

                    return savedFoodItem.getId();
                })
                .collect(Collectors.toList());
        DeviceResponse deviceResponse = new DeviceResponse(device.getId(), listFoodItemsId);
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.toString(), "Device Id", deviceResponse));
    }

}
