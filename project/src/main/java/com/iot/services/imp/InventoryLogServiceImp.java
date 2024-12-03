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

        List<InventoryLog> inventoryLogs = inventoryLogRepository.getInventoryLogByCompany(companyId, startOfDay, endOfDay);

        Map<Long, InventoryLog> latestLogs = new HashMap<>();

        for (InventoryLog log : inventoryLogs) {
            Long foodItemId = Long.valueOf(log.getFoodItemId());
            Date createdAt = log.getCreated_at();

            if (!latestLogs.containsKey(foodItemId)) {
                latestLogs.put(foodItemId, log);
            } else {
                InventoryLog existingLog = latestLogs.get(foodItemId);
                if (createdAt.after(existingLog.getCreated_at())) {
                    latestLogs.put(foodItemId, log);
                }
            }
        }

        List<InventoryLog> filteredLogs = new ArrayList<>(latestLogs.values());

        return ResponseEntity.ok(new ResponseObject(Validation.OK, "Success!", filteredLogs));
    }

    @Override
    public ResponseEntity<ResponseObject> getConsumedQuantity(Long companyId, Date specificDate) {
        Date startOfDay = Validation.startOfDay(specificDate);
        Date endOfDay = Validation.endOfDay(specificDate);

        List<FoodItem> foodItemList = foodItemRepository.getAllFoodItem(Math.toIntExact(companyId));

        Map<String, ConsumeQuantity> consumptionByName = new HashMap<>();

        for (FoodItem foodItem : foodItemList) {
            List<InventoryLog> inventoryLogToday = inventoryLogRepository.findByFoodItemIdAndCreatedAt(
                    foodItem.getId(), startOfDay, endOfDay);
            for (InventoryLog inventoryLog : inventoryLogToday){
                if (inventoryLog.getConsumedQuantity() > 0) {
                    consumptionByName.computeIfAbsent(inventoryLog.getFoodName(), name -> new ConsumeQuantity(name, 0.0))
                            .addConsumeQuantity(inventoryLog.getConsumedQuantity());
                }
            }
        }

        List<ConsumeQuantity> result = new ArrayList<>(consumptionByName.values());

        return ResponseEntity.ok(new ResponseObject(Validation.OK, "Success!", result));
    }
}
