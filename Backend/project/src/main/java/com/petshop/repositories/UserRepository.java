package com.petshop.repositories;

import com.petshop.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.UserName = :UserName")
    Optional<User> findByUserName(@Param("UserName") String UserName);
    Optional<User> findById(Long user_id);

    @Query("SELECT u FROM User u WHERE u.Email = :Email")
    Optional<User> findByEmail(@Param("Email") String Email);
    @Query("SELECT u FROM User u WHERE u.PhoneNumber = :PhoneNumber")
    Optional<User> findByPhone(@Param("PhoneNumber") String PhoneNumber);
    @Query("select u from User u INNER join u.online_status o  where o.status =:status")
    public List<User> findAllUserOnline(@Param("status") Long status);
    @Query("select u,u.online_status.status from User INNER join u.online_status ")
    public List<Object[]> getListUserWithStatus();
}
