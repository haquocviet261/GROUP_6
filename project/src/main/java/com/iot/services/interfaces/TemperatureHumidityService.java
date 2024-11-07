package com.iot.services.interfaces;

import com.iot.model.entity.TemperatureHumidity;
import org.springframework.stereotype.Service;

import java.util.List;
public interface TemperatureHumidityService {
    List<TemperatureHumidity> getAllTemperatureHumidityByCompanyId(Long company_id);
}
