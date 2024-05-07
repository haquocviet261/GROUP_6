package com.petshop.services.imp;

import com.petshop.model.entity.ChatMessage;
import com.petshop.model.entity.ChatNotification;
import com.petshop.model.entity.Conversation;
import com.petshop.repositories.ChatMessageRepository;
import com.petshop.repositories.ConversationRepository;
import com.petshop.services.interfaces.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChatMessageServiceImp implements ChatMessageService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    ChatMessageRepository chatMessageRepository;
    @Autowired
    ConversationServiceImp conversationServiceImp;
    @Autowired
    ConversationRepository conversationRepository;


    @Override
    public ChatMessage save(ChatMessage chatMessage) {
        String chat_id = conversationServiceImp.getConversationID(chatMessage.getSender_id().getUser_id(), chatMessage.getReceiver_id().getUser_id(),true).orElseThrow();
        Optional<Conversation> conversation =  conversationRepository.findById(chat_id);
        chatMessage.setConversation(conversation.get());
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }

    @Override
    public List<ChatMessage> findChatMessage(Long sender_id, Long receiver_id) {
        String conversation_id = conversationServiceImp.getConversationID(sender_id, receiver_id, false).orElseThrow();
        return chatMessageRepository.findChatMessageByconversationID(conversation_id)
                .orElse(new ArrayList<>());
    }
    public void processMessage(ChatMessage chatMessage){
        ChatMessage message = chatMessageRepository.save(chatMessage);
        messagingTemplate.convertAndSendToUser(String.valueOf(chatMessage.getReceiver_id().getUser_id()),"queue/messages", ChatNotification.builder().sender_id(chatMessage.getSender_id().getUser_id()).receiver_id(
                chatMessage.getReceiver_id().getUser_id())
                .chat_notification_id(chatMessage
                        .getMessage_id()).build());
    }
}
