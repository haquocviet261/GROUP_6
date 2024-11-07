package com.iot.services.imp;

import com.iot.model.dto.response.ResponseObject;
import com.iot.services.interfaces.StatisticService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
@Service
public class StatisticServiceImp implements StatisticService {
    @Override
    public ResponseEntity<ResponseObject> getFoodExpiredByCompanyId(Long company_id) {
        return null;
    }
}
