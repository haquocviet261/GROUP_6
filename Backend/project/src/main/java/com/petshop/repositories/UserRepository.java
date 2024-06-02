package com.petshop.repositories;

import com.petshop.model.dto.response.UserStatusResponse;
import com.petshop.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.user_name = :user_name")
    Optional<User> findByUserName(@Param("user_name") String user_name);
    Optional<User> findById(Long user_id);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);
    @Query("SELECT u FROM User u WHERE u.phone_number = :phone_number")
    Optional<User> findByPhone(@Param("phone_number") String phone_number);
    @Query("select u from User u INNER join u.online_status o  where o.status =:status")
    public List<User> findAllUserOnline(@Param("status") Long status);
    @Query("select u,u.online_status.status from User u INNER join u.online_status ")
    public List<Object[]> getListUserWithStatus();
    @Query("select new com.petshop.model.dto.response.UserStatusResponse(u.user_id,u.user_name,u.first_name,u.last_name,u.image_src,s.status) from User u INNER join u.online_status s where u.user_id =:user_id")
    public UserStatusResponse getUserWithStatus(@Param("user_id") Long user_id);
}
