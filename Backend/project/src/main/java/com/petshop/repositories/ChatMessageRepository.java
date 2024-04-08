package com.petshop.repositories;

import com.petshop.models.entities.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {
    @Query("select c from ChatMessage c where sender_id.UserId =:sender_id and receiver_id.UserId=:receiver_id")
    public List<ChatMessage> findChatMessage(@Param("sender_id")Long sender_id,@Param("receiver_id") Long receiver_id);
    @Query("select c from ChatMessage c where conversation.conversation_id=:conversation_id")
    public Optional<List<ChatMessage>> findChatMessageByconversationID(@Param("conversation_id") String conversation_id);
}
