package com.iot.repositories;

import com.iot.model.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company,Long> {
    @Query("SELECT c.name FROM Company c")
    List<String> getAllCompany();

    @Query("SELECT c FROM Company c WHERE c.name = :name")
    Optional<Company> getByCompanyName(@Param("name") String name);

}
