package com.petshop.services.imp;

import com.petshop.model.entity.Conversation;
import com.petshop.repositories.ConversationRepository;
import com.petshop.repositories.UserRepository;
import com.petshop.services.interfaces.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConversationServiceImp implements ConversationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConversationRepository conversationRepository;

    @Override
    public Optional<String> getConversationID(Long user_id, Long admin_id,boolean createNewConversationIfNotExist) {
        return conversationRepository.findbySenderIDAndRecevieID(user_id,admin_id).map(Conversation::getConversation_id).or(() -> {
           if (createNewConversationIfNotExist){
               String conversation_id = createChatID(user_id,admin_id);
               return  Optional.of(conversation_id);
           }
            return Optional.empty();
        });
    }

    private String createChatID(Long userId, Long adminId) {
        String chat_id = String.format("%s_%S",userId,adminId);
        Conversation user = Conversation.builder()
        .user(userRepository.findById(userId).orElseThrow())
        .admin(userRepository.findById(adminId).orElseThrow()).build();

        Conversation admin = Conversation.builder()
        .user(userRepository.findById(adminId).orElseThrow())
        .admin(userRepository.findById(userId).orElseThrow()).build();
        conversationRepository.save(user);
        conversationRepository.save(admin);
        return chat_id;

    }
}
