package com.iot.services.interfaces;

import com.iot.model.entity.TemperatureHumidity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

public interface TemperatureHumidityService {
    List<TemperatureHumidity> getAllTemperatureHumidityByCompanyId(Long company_id);

    List<TemperatureHumidity> getDataForSpecificDay(Long companyId, Date specificDate);
}
