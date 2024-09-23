package com.iot.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "FoodItem")
public class FoodItem extends CommonEntity{
    private String name;
    private Integer food_id;
    private Date add_date;
    private String type_unit;
    private String expired_date;
    private String quantity;
    private Integer device_id;
}
