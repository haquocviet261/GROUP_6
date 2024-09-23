package com.iot.repositories;

import com.iot.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.id = :user_name")
    Optional<User> findByUserName(@Param("user_name") String user_name);
    @Query("SELECT u FROM User u WHERE u.id = :user_id")
    Optional<User> findById(@Param("user_id") Long user_id);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);
    @Query("SELECT u FROM User u WHERE u.phone_number = :phone_number")
    Optional<User> findByPhone(@Param("phone_number") String phone_number);
    @Query("SELECT u FROM User u WHERE u.user_name = :user_name")
    List<User> findByUserNameList(@Param("user_name") String user_name);

}
