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
public class FoodCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long food_category_id;

    @Column(nullable = false)
    private String category_name;

    @OneToMany(mappedBy = "foodCategory",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<Food> foods;
    public FoodCategory(String category_name) {
        this.category_name = category_name;
    }
}
