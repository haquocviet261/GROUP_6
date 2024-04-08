package com.petshop.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatNotification {
    private Long chat_notification_id;
    private Long sender_id;
    private Long receiver_id;
    private String content;
}
