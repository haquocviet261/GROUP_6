package com.iot.services.imp;

import com.iot.model.entity.TemperatureHumidity;
import com.iot.repositories.TemperatureHumidityRepository;
import com.iot.services.interfaces.TemperatureHumidityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TemperatureHumidityServiceImp implements TemperatureHumidityService {
    @Autowired
    private TemperatureHumidityRepository temperatureHumidityRepository;
    @Override
    public List<TemperatureHumidity> getAllTemperatureHumidityByCompanyId(Long company_id) {
        return temperatureHumidityRepository.getAllTemperatureHumidityByCompanyId(company_id);
    }
}
