package com.petshop.controller;

import com.petshop.model.dto.response.ResponseObject;
import com.petshop.services.imp.ConversationServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/conversation")

public class ConversationController {
    @Autowired
    private ConversationServiceImp conversationServiceImp;
    @GetMapping("/all")
    public ResponseEntity<ResponseObject> getAllConversation(@RequestParam(required = true) Long user_id){
        return  conversationServiceImp.getAllConversationBySenderID(user_id);
    }
}
