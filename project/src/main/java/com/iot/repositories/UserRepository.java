package com.iot.repositories;

import com.iot.model.dto.request.UserDTO;
import com.iot.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.user_name = :user_name AND u.deleted_at IS NULL")
    Optional<User> findByUserName(@Param("user_name") String user_name);

    @Query("SELECT u FROM User u WHERE u.id = :user_id AND u.deleted_at IS NULL")
    Optional<User> findById(@Param("user_id") Long user_id);

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.deleted_at IS NULL")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.phone_number = :phone_number AND u.deleted_at IS NULL")
    Optional<User> findByPhone(@Param("phone_number") String phone_number);

    @Query("SELECT u FROM User u WHERE u.user_name = :user_name AND u.deleted_at IS NULL")
    List<User> findByUserNameList(@Param("user_name") String user_name);

    @Query("SELECT new com.iot.model.dto.request.UserDTO(u.user_name, u.password) FROM User u Where u.deleted_at IS NULL")
    List<UserDTO> getAllUser();

    @Query("SELECT u FROM User u WHERE " +
            "u.user_name LIKE %:keyword% OR " +
            "u.email LIKE %:keyword% OR " +
            "u.id IN (SELECT c.user_id FROM Company c WHERE c.name LIKE %:keyword%)")
    List<User> searchUsers(@Param("keyword") String keyword);

}
