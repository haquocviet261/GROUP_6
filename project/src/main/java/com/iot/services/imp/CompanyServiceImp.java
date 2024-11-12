package com.iot.services.imp;

import com.iot.common.utils.Validation;
import com.iot.model.dto.response.ResponseObject;
import com.iot.repositories.CompanyRepository;
import com.iot.services.interfaces.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CompanyServiceImp implements CompanyService {
    @Autowired
    CompanyRepository companyRepository;

    @Override
    public ResponseEntity<ResponseObject> getAllNameCompany() {
        return ResponseEntity.ok(new ResponseObject(Validation.OK,"Successfully !!!", companyRepository.getAllCompany()));
    }
}
