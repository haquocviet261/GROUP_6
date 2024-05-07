package com.petshop.services.interfaces;

import java.util.Optional;

public interface ConversationService {

    public Optional<String> getConversationID(Long user_id,Long admin_id,boolean createNewConversationIfNotExist);
}
