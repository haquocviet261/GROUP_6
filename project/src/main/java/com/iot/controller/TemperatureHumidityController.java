package com.iot.controller;

import com.iot.model.dto.response.ResponseObject;
import com.iot.services.interfaces.TemperatureHumidityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("api/temperature-humidity")
public class TemperatureHumidityController {
    @Autowired
    private TemperatureHumidityService temperatureHumidityService;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ResponseObject getTemperatureHumidityByCompanyId(@RequestParam Long company_id) {
        return new ResponseObject(HttpStatus.OK.toString(), "List TemperatureHumidity", temperatureHumidityService.getAllTemperatureHumidityByCompanyId(company_id));
    }

    @GetMapping("/get-by-date")
    public ResponseObject getTemperatureHumidityBySpecificDay(
            @RequestParam Long companyId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date specificDate) {

        return new ResponseObject(
                HttpStatus.OK.toString(),
                "Temperature and Humidity data for specific day",
                temperatureHumidityService.getDataForSpecificDay(companyId, specificDate)
        );
    }
}
