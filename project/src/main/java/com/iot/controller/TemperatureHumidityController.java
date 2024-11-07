package com.iot.controller;

import com.iot.model.dto.response.ResponseObject;
import com.iot.services.interfaces.TemperatureHumidityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/temperature-humidity")
public class TemperatureHumidityController {
    @Autowired
    private TemperatureHumidityService temperatureHumidityService;
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ResponseObject getTemperatureHumidityByconpanyId(@RequestParam Long company_id) {
        return new ResponseObject(HttpStatus.OK.toString(), "List TemperatureHumidity", temperatureHumidityService.getAllTemperatureHumidityByCompanyId(company_id));
    }
}
