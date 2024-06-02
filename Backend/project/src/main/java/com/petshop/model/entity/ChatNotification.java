package com.petshop.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatNotification {
    private Long chat_notification_id;
    private Long sender_id;
    private Long receiver_id;
    private String content;
    private LocalDateTime current_sent;
}
