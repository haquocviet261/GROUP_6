package com.petshop.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Table(name = "FoodItem")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long food_item_id;
    private String food_name;
    private Date add_date;
    private String unit;
    private Date expiration_date;
    @ManyToOne
    @JsonBackReference
    @JsonIgnore
    @JoinColumn(name ="food_id")
    private Food food;
    @Transient
    public Long getFood_id(){
        return food != null ? food.getFood_id() : null;
    }
    @Transient
    public Long getDevice_item_id(){
        return deviceItem != null ? deviceItem.getDevice_item_id() : null;
    }
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "device_item_id")
    private DeviceItem deviceItem;

}
