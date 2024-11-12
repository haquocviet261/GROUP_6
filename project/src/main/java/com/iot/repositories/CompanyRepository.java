package com.iot.repositories;

import com.iot.model.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company,Long> {
    @Query("SELECT c.name FROM Company c")
    List<String> getAllCompany();
}
