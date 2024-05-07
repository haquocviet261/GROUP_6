package com.petshop.repositories;

import com.petshop.model.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query("SELECT t FROM Token t JOIN t.user u WHERE u.user_id = :user_id AND (t.expired = false OR t.revoked = false)")
    List<Token> findAllValidTokenByUser(@Param("user_id") Long user_id);
    Optional<Token> findByToken(String token);
}
