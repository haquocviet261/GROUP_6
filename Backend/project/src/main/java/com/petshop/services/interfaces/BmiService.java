package com.petshop.services.interfaces;

import com.petshop.model.dto.request.BmiRequest;
import com.petshop.model.dto.response.ResponseObject;
import com.petshop.model.entity.Bmi;
import org.springframework.http.ResponseEntity;

public interface BmiService {
    ResponseEntity<ResponseObject> addBmi(BmiRequest bmiRequest);
    ResponseEntity<ResponseObject> getBmiBeyUserID(Long user_id);
}
