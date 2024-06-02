package com.petshop.controller;


import com.petshop.model.dto.response.ResponseObject;
import com.petshop.model.entity.ChatMessage;
import com.petshop.services.imp.ChatMessageServiceImp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequestMapping("api/message")
public class ChatController {

    @Autowired
    private ChatMessageServiceImp chatMessageServiceImp;
    @MessageMapping("/chat")
    public void ProcessMessage(@Payload ChatMessage chatMessage){
        log.info(chatMessage.toString());
        chatMessageServiceImp.processMessage(chatMessage);
    }
    @GetMapping("/{sender_id}/{receiver_id}")
    public ResponseEntity<ResponseObject> findChatMessage(@RequestParam("page") Integer page,@RequestParam("size") Integer size, @PathVariable("sender_id") Long sender_id, @PathVariable("receiver_id") Long receiver_id){
        return ResponseEntity.ok(new ResponseObject("OK","List Chat message",chatMessageServiceImp.findChatMessages(page,size,sender_id,receiver_id)));
    }
}
