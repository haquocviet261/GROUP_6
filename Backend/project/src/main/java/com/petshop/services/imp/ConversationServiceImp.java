package com.petshop.services.imp;

import com.petshop.model.dto.response.ResponseObject;
import com.petshop.model.entity.Conversation;
import com.petshop.repositories.ConversationRepository;
import com.petshop.repositories.UserRepository;
import com.petshop.services.interfaces.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
               String conversation_id = createConversationID(user_id,admin_id);
               return  Optional.of(conversation_id);
           }
            return Optional.empty();
        });
    }

    @Override
    public ResponseEntity<ResponseObject> getAllConversationBySenderID(Long user_id) {
        return ResponseEntity.ok(new ResponseObject("OK","List Conversation",conversationRepository.getAllConversation(user_id)));
    }

    private String createConversationID(Long sender_id, Long receiver_id) {
        String chat_id = String.format("%s_%S",sender_id,receiver_id);
        Conversation senderToReceiver = Conversation.builder()
                .conversation_id(chat_id)
                .last_message("")

        .sender(userRepository.findById(sender_id).orElseThrow())
        .receiver(userRepository.findById(receiver_id).orElseThrow()).build();
        Conversation receiverToSender = Conversation.builder()
                .conversation_id(chat_id)
                .last_message("")
        .sender(userRepository.findById(receiver_id).orElseThrow())
        .receiver(userRepository.findById(sender_id).orElseThrow()).build();
        conversationRepository.save(senderToReceiver);
        conversationRepository.save(receiverToSender);
        return chat_id;

    }
}
