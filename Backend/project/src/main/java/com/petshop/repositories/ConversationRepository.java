package com.petshop.repositories;

import com.petshop.model.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation,String> {
    @Query("select c from Conversation c where c.user.user_id =:user_id and c.admin.user_id =:admin_id")
     Optional<Conversation> findbySenderIDAndRecevieID(@Param("user_id") Long user_id, @Param("admin_id") Long admin_id);
}
