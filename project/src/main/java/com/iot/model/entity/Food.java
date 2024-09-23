package com.iot.model.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Food")
public class Food extends CommonEntity{
    private String name;
    private Integer category_id;
    private Integer expired_date;
}
