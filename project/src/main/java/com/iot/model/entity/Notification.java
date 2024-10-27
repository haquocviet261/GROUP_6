package com.iot.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Notification")
public class Notification extends CommonEntity{
    @Column(name = "type_notification")
    private String type_notification;
    @Column(name = "message")
    private String message;
    @Column(name = "user_id")
    private Long user_id;
}
