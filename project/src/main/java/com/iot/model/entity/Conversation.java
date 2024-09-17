package com.iot.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Conversation")
public class Conversation  {
    @Id
    @Column(name = "conversation_id")
    private String conversation_id;
    @Column(name = "status")
    private Integer status;
    private String last_message;
    @Column(columnDefinition = "DATETIME2 DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
    private LocalDateTime last_message_timestamp;
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;
}
