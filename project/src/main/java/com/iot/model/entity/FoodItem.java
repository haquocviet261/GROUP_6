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
@Table(name = "FoodItem")
public class FoodItem extends CommonEntity{
    @Column(name = "food_id")
    private Integer food_id;
    @Column(name = "name")
    private String name;
    @Column(name = "type_unit")
    private String type_unit;
    @Column(name = "expired_date")
    private Integer expired_date;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "company_id")
    private Integer company_id;
}
