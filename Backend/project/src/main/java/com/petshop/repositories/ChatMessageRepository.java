package com.petshop.repositories;

import com.petshop.model.entity.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,String> {
    @Query("select c from ChatMessage c where c.sender_id =:sender_id and c.receiver_id=:receiver_id")
    public List<ChatMessage> findChatMessage(@Param("sender_id")Long sender_id, @Param("receiver_id") Long receiver_id);
    @Query("select c from ChatMessage c where c.conversation_id=:sender_conversation_receiver_id or c.conversation_id=:receiver_conversation_sender_id order by message_id desc")
    public Optional<List<ChatMessage>> findChatMessageByconversationID(Pageable pageable, @Param("sender_conversation_receiver_id") String sender_conversation_receiver_id,
                                                                       @Param("receiver_conversation_sender_id") String receiver_conversation_sender_id);
}
