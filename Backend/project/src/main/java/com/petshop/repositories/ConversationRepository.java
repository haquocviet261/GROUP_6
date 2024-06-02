package com.petshop.repositories;

import com.petshop.model.dto.response.ConversationResponse;
import com.petshop.model.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation,String> {
    @Query("select c from Conversation c where c.sender.user_id =:sender_id and c.receiver.user_id =:receiver_id")
     Optional<Conversation> findbySenderIDAndRecevieID(@Param("sender_id") Long sender_id, @Param("receiver_id") Long receiver_id);
    @Query("select new com.petshop.model.dto.response.ConversationResponse(c.conversation_id, c.last_message, c.last_message_timestamp," +
            " c.sender.user_name, c.receiver.image_src, c.sender.user_id, c.receiver.user_id) " +
            "from Conversation c where c.sender.user_id = :user_id or c.receiver.user_id = :user_id")
    List<ConversationResponse> getAllConversation(@Param("user_id") Long user_id);

}
