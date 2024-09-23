package com.iot.model.entity;

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
public class FoodNotification extends CommonEntity{
    private String notification_type;
    private String message;
    private Long user_id;
}
