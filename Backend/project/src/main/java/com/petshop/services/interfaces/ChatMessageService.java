package com.petshop.services.interfaces;


import com.petshop.model.entity.ChatMessage;

import java.util.List;

public interface ChatMessageService {
    public ChatMessage save(ChatMessage chatMessage);
    public List<ChatMessage> findChatMessage(Long sender_id,Long receiver_id);
    public void processMessage(ChatMessage chatMessage);
}
