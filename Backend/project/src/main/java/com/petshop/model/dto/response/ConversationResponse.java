package com.petshop.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversationResponse {
    private String conversation_id;
    private String last_message;
    private LocalDateTime last_message_timestamp;
    private String user_name;
    private String user_image_src;
    private Long sender_id;
    private Long receiver_id;
}
