package com.petshop.controller;

import com.petshop.models.dto.response.ResponseObject;
import com.petshop.models.entities.ChatMessage;
import com.petshop.services.imp.ChatMessageServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
public class ChatController {

    @Autowired
    ChatMessageServiceImp chatMessageServiceImp;
    @MessageMapping("/chat")
    public void ProcessMessage(@Payload ChatMessage chatMessage){
        chatMessageServiceImp.processMessage(chatMessage);
    }
    @GetMapping("/{sender_id}/{receiver_id}")
    public ResponseEntity<ResponseObject> findChatMessage(@PathVariable("sender_id") Long sender_id, @PathVariable("receiver_id") Long receiver_id){
        return ResponseEntity.ok(new ResponseObject("OK","List Chat message",chatMessageServiceImp.findChatMessage(sender_id,receiver_id)));
    }
}
