package com.petshop.services.interfaces;

import com.petshop.model.dto.response.ResponseObject;
import com.petshop.model.entity.Conversation;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface ConversationService {

    public Optional<String> getConversationID(Long user_id,Long admin_id,boolean createNewConversationIfNotExist);
    ResponseEntity<ResponseObject> getAllConversationBySenderID(Long sender_id);
}
