package com.petshop.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Table(name = "FoodNotification")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notification_id;
    private String message;
    private Date notification_time;
}
