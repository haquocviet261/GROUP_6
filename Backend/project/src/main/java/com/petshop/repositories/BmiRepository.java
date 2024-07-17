package com.petshop.repositories;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.petshop.common.constant.Gender;
import com.petshop.model.dto.response.BmiResponse;
import com.petshop.model.entity.Bmi;
import com.petshop.model.entity.User;
import jakarta.persistence.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BmiRepository extends JpaRepository<Bmi,Long> {
    @Query("select new com.petshop.model.dto.response.BmiResponse(b.weight,b.height,b.age,b.gender,b.user.user_id) from Bmi b where b.user.user_id = :user_id")
    BmiResponse getBmiByUserID(@Param("user_id") Long user_id);
}

