package com.iot.repositories;

import com.iot.model.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query("SELECT t FROM Token t JOIN User u ON u.id = t.user_id WHERE u.id = :user_id AND (t.expired = false OR t.revoked = false)")
    List<Token> findAllValidTokenByUser(@Param("user_id") Long user_id);
    Optional<Token> findByToken(String token);
}
