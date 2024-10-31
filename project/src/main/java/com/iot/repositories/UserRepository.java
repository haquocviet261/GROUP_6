package com.iot.repositories;

import com.iot.model.dto.response.UserResponse;
import com.iot.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.user_name = :user_name AND u.deleted_at IS NULL AND u.status = 'ACTIVE'")
    Optional<User> findByUserName(@Param("user_name") String user_name);

    @Query("SELECT u FROM User u WHERE u.id = :user_id AND u.deleted_at IS NULL AND u.status = 'ACTIVE'")
    Optional<User> findById(@Param("user_id") Long user_id);

    @Query("SELECT u FROM User u WHERE u.id = :user_id AND u.deleted_at IS NULL")
    Optional<User> findById2(@Param("user_id") Long user_id);

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.deleted_at IS NULL AND u.status = 'ACTIVE'")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.phone_number = :phone_number AND u.deleted_at IS NULL AND u.status = 'ACTIVE'")
    Optional<User> findByPhone(@Param("phone_number") String phone_number);

    @Query("SELECT u FROM User u WHERE u.user_name = :user_name AND u.deleted_at IS NULL AND u.status = 'ACTIVE'")
    List<User> findByUserNameList(@Param("user_name") String user_name);

    @Query("SELECT new com.iot.model.dto.response.UserResponse(u.id, u.user_name, u.firstname, u.lastname, u.phone_number," +
            " u.date_of_birth, u.email, u.address, u.role, u.status, u.images_src, c.name) " +
            "FROM User u JOIN Company c ON u.id = c.user_id  " +
            "WHERE u.deleted_at IS NULL AND u.status = 'ACTIVE'")
    List<UserResponse> getAllUserForAdmin();

    @Query("SELECT new com.iot.model.dto.response.UserResponse(u.id, u.user_name, u.firstname, u.lastname, u.phone_number," +
            " u.date_of_birth, u.email, u.address, u.role, u.status, u.images_src, c.name) " +
            "FROM User u JOIN Company c ON u.id = c.user_id  " +
            "WHERE u.deleted_at IS NULL AND u.status = 'ACTIVE' AND c.name = :company_name AND LOWER(u.role) != LOWER('Manager')")
    List<UserResponse> getAllUserByCompanyExceptManager(@Param("company_name") String company_name);

    @Query("SELECT c.name FROM User u JOIN Company c ON u.id = c.user_id WHERE u.deleted_at IS NULL AND u.status = 'ACTIVE' AND u.id = :user_id")
    String getCompanyNameIdByUserId(@Param("user_id") Long user_id);

    @Query("SELECT new com.iot.model.dto.response.UserResponse(u.id, u.user_name, u.firstname, u.lastname, u.phone_number," +
            " u.date_of_birth, u.email, u.address, u.role, u.status, u.images_src, c.name) " +
            "FROM User u JOIN Company c ON u.id = c.user_id " +
            "WHERE (u.user_name LIKE %:keyword% OR " +
            "u.email LIKE %:keyword% OR " +
            "c.name LIKE %:keyword% OR " +
            "u.phone_number LIKE %:keyword%) " +
            "AND u.deleted_at IS NULL AND u.status = 'ACTIVE'")
    List<UserResponse> searchUsersForAdmin(@Param("keyword") String keyword);

    @Query("SELECT new com.iot.model.dto.response.UserResponse(u.id, u.user_name, u.firstname, u.lastname, u.phone_number," +
            " u.date_of_birth, u.email, u.address, u.role, u.status, u.images_src, c.name) " +
            "FROM User u JOIN Company c ON u.id = c.user_id " +
            "WHERE (u.user_name LIKE %:keyword% OR " +
            "u.email LIKE %:keyword% OR " +
            "u.phone_number LIKE %:keyword%) " +
            "AND u.deleted_at IS NULL AND u.status = 'ACTIVE' " +
            "AND c.name = :company_name AND LOWER(u.role) != LOWER('Manager')")
    List<UserResponse> searchUsersForManager(@Param("keyword") String keyword, @Param("company_name") String company_name);

}
