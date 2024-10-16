package com.iot.model.entity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Food")
public class Food extends CommonEntity{
    @Column(name = "name")
    private String name;
    @Column(name = "category_id")
    private Integer category_id;
    @Column(name = "expired_date")
    private Integer expired_date;
}
