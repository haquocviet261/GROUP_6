package com.petshop.repositories;

import com.petshop.models.entities.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation,String> {
    @Query("select c from Conversation c where c.user.UserId =:user_id and c.admin.UserId =:admin_id")
    public Optional<Conversation> findbySenderIDAndRecevieID(@Param("user_id") Long user_id,@Param("admin_id") Long admin_id);
}
