package com.iot.services.imp;

import com.iot.common.utils.Validation;
import com.iot.model.entity.TemperatureHumidity;
import com.iot.repositories.TemperatureHumidityRepository;
import com.iot.services.interfaces.TemperatureHumidityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
@Service
public class TemperatureHumidityServiceImp implements TemperatureHumidityService {
    @Autowired
    private TemperatureHumidityRepository temperatureHumidityRepository;
    @Override
    public List<TemperatureHumidity> getAllTemperatureHumidityByCompanyId(Long company_id) {
        return temperatureHumidityRepository.getAllByCompanyIdAndCreatedAtAsc(company_id);
    }

    public List<TemperatureHumidity> getDataForSpecificDay(Long companyId, Date specificDate) {
        Date startOfDay = Validation.startOfDay(specificDate);
        Date endOfDay = Validation.endOfDay(specificDate);
        return temperatureHumidityRepository.getAllByCompanyIdAndDateRange(companyId, startOfDay, endOfDay);
    }

}
