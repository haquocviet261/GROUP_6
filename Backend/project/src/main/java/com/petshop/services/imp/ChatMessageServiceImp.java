package com.petshop.services.imp;

import com.petshop.model.entity.ChatMessage;
import com.petshop.model.entity.ChatNotification;
import com.petshop.model.entity.Conversation;
import com.petshop.model.entity.User;
import com.petshop.repositories.ChatMessageRepository;
import com.petshop.repositories.ConversationRepository;
import com.petshop.repositories.UserRepository;
import com.petshop.services.interfaces.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ChatMessageServiceImp implements ChatMessageService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    ChatMessageRepository chatMessageRepository;
    @Autowired
    ConversationServiceImp conversationServiceImp;
    @Autowired
    ConversationRepository conversationRepository;


    @Override
    public ChatMessage save(ChatMessage chatMessage) {
        String chat_id = conversationServiceImp.getConversationID(chatMessage.getSender_id(), chatMessage.getReceiver_id(),true).orElseThrow();
        Optional<Conversation> conversation =  conversationRepository.findById(chat_id);
        chatMessage.setConversation_id(conversation.get().getConversation_id());
        ChatMessage savedChatMessage = chatMessageRepository.save(chatMessage);
        return savedChatMessage;
    }

    public List<ChatMessage> findChatMessages(Integer page,Integer size,Long sender_id, Long receiver_id) {
        String sender_conversation_receiver_id = conversationServiceImp.getConversationID(sender_id, receiver_id, false).orElse("");
        String receiver_conversation_sender_id = conversationServiceImp.getConversationID(receiver_id, sender_id, false).orElse("");
        Optional<List<ChatMessage>> list = chatMessageRepository.findChatMessageByconversationID(PageRequest.of(page,size),sender_conversation_receiver_id,receiver_conversation_sender_id);
        Collections.reverse(list.orElse(new ArrayList<>()));
        return list.orElse(new ArrayList<>());
    }
    public void processMessage(ChatMessage chatMessage){
         save(chatMessage);
        User sender = userRepository.findById(chatMessage.getSender_id()).orElseThrow();
        User receiver = userRepository.findById(chatMessage.getReceiver_id()).orElseThrow();

        Conversation conversation = conversationRepository.findbySenderIDAndRecevieID(chatMessage.getSender_id(),chatMessage.getReceiver_id())
                 .orElse(new Conversation());
         conversation.setConversation_id(conversationServiceImp.getConversationID(chatMessage.getSender_id(), chatMessage.getReceiver_id(), true).orElseThrow());
         conversation.setLast_message(chatMessage.getContent());
         conversation.setLast_message_timestamp(LocalDateTime.now());
         conversation.setSender(sender);
         conversation.setReceiver(receiver);
         conversationRepository.save(conversation);

         messagingTemplate.convertAndSendToUser(String.valueOf(chatMessage.getReceiver_id())
                ,"queue/messages", ChatNotification.builder().sender_id(chatMessage.getSender_id()).receiver_id(
                chatMessage.getReceiver_id())
                                 .content(chatMessage.getContent())
                .chat_notification_id(chatMessage
                        .getMessage_id())
                         .build());
    }
}
