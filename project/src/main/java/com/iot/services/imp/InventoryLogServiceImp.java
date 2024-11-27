package com.iot.services.imp;

import com.iot.common.utils.Validation;
import com.iot.model.dto.response.ConsumeQuantity;
import com.iot.model.dto.response.ResponseObject;
import com.iot.model.entity.FoodItem;
import com.iot.model.entity.InventoryLog;
import com.iot.repositories.FoodItemRepository;
import com.iot.repositories.InventoryLogRepository;
import com.iot.services.interfaces.InventoryLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class InventoryLogServiceImp implements InventoryLogService {
    @Autowired
    InventoryLogRepository inventoryLogRepository;
    @Autowired
    FoodItemRepository foodItemRepository;


    @Override
    public ResponseEntity<ResponseObject> getAllInventoryLog(Long companyId, Date specificDate) {
        Date startOfDay = Validation.startOfDay(specificDate);
        Date endOfDay = Validation.endOfDay(specificDate);
        return ResponseEntity.ok(new ResponseObject(Validation.OK,"Success!",
                inventoryLogRepository.getInventoryLogByCompany(companyId,startOfDay,endOfDay)));
    }

    @Override
    public ResponseEntity<ResponseObject> getConsumedQuantity(Long companyId, Date specificDate) {
        Date startOfDay = Validation.startOfDay(specificDate);
        Date endOfDay = Validation.endOfDay(specificDate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(specificDate);
        calendar.add(Calendar.DATE, -1);
        Date oneDayBefore = calendar.getTime();
        Date startOfDayBefore = Validation.startOfDay(oneDayBefore);
        Date endOfDayBefore = Validation.endOfDay(oneDayBefore);

        List<FoodItem> foodItemList = foodItemRepository.getAllFoodItem(Math.toIntExact(companyId));

        Map<String, ConsumeQuantity> consumptionByName = new HashMap<>();

        for (FoodItem foodItem : foodItemList) {
            Optional<InventoryLog> inventoryLogToday = inventoryLogRepository.findByFoodItemIdAndCreatedAt(
                    foodItem.getId(), startOfDay, endOfDay);

            Optional<InventoryLog> inventoryLogYesterday = inventoryLogRepository.findByFoodItemIdAndCreatedAt(
                    foodItem.getId(), startOfDayBefore, endOfDayBefore);

            double openingQuantity = inventoryLogYesterday.map(log -> log.getClosingQuantity() != null ? log.getClosingQuantity() : 0.0).orElse(0.0);
            double closingQuantity = inventoryLogToday.map(log -> log.getClosingQuantity() != null ? log.getClosingQuantity() : 0.0).orElse(0.0);
            double addedQuantity = inventoryLogToday.map(log -> log.getAddedQuantity() != null ? log.getAddedQuantity() : 0.0).orElse(0.0);
            double consumedQuantity = openingQuantity - closingQuantity + addedQuantity;

            if (consumedQuantity > 0) {
                consumptionByName.computeIfAbsent(foodItem.getName(), name -> new ConsumeQuantity(name, 0.0))
                        .addConsumeQuantity(consumedQuantity);
            }
        }

        List<ConsumeQuantity> result = new ArrayList<>(consumptionByName.values());

        return ResponseEntity.ok(new ResponseObject(Validation.OK, "Success!", result));
    }
}
