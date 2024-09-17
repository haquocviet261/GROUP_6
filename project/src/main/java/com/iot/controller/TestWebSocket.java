package com.iot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;


@Controller
public class TestWebSocket {
    private static final Logger logger = LoggerFactory.getLogger(TestWebSocket.class);

    @MessageMapping("/ws")
    @SendTo("/topic/greetings")
    public String greeting(String message) {
        logger.info("Received message from client: {}", message);
        return "Hello, " + message + "!";
    }
}
