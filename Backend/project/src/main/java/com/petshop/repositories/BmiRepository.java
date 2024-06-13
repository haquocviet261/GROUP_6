package com.petshop.repositories;

import com.petshop.model.entity.Bmi;
import com.petshop.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BmiRepository extends JpaRepository<Bmi,Long> {
    @Query("select b from Bmi b inner join b.user u where u.user_id = :user_id")
    Bmi getBmiByUserID(@Param("user_id") Long user_id);
}
