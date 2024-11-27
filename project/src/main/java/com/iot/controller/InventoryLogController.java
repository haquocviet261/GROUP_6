package com.iot.controller;

import com.iot.model.dto.response.ResponseObject;
import com.iot.services.imp.InventoryLogServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("api/inventory-log")
public class InventoryLogController {
    @Autowired
    InventoryLogServiceImp inventoryLogServiceImp;


    @GetMapping("/get-by-date")
    public ResponseEntity<ResponseObject> getAllInventoryLogBySpecificDay(
            @RequestParam Long companyId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date specificDate) {

        return inventoryLogServiceImp.getAllInventoryLog(companyId, specificDate);
    }

    @GetMapping("/get-consumed-quantity")
    public ResponseEntity<ResponseObject> getConsumedQuantity(
            @RequestParam Long companyId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date specificDate) {

        return inventoryLogServiceImp.getConsumedQuantity(companyId, specificDate);
    }
}
