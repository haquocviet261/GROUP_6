package com.petshop.services.interfaces;

import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ConversationService {

    public Optional<String> getConversationID(Long user_id,Long admin_id,boolean createNewConversationIfNotExist);
}
