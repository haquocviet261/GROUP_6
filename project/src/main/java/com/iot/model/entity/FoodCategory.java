package com.iot.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "FoodCategory")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodCategory extends CommonEntity{
    private String category_name;

}
