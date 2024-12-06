package com.iot.controller;

import com.iot.model.dto.response.ResponseObject;
import com.iot.services.imp.CompanyServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/company")
public class CompanyController {
    @Autowired
    CompanyServiceImp companyServiceImp;

    @GetMapping("/all")
    ResponseEntity<ResponseObject> getAllNameCompany(){
        return companyServiceImp.getAllNameCompany();
    }
    @GetMapping("/get")
    public ResponseEntity<ResponseObject> getAllCompany() {
        return companyServiceImp.getAllCompany();
    }
}
