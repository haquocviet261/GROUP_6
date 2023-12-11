package com.petshop.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long product_id;
    @ManyToOne
    @JoinColumn(name = "sub_category_id")
    private SubCategory subCategory;
    private String product_name;
    private int quantity;
    private double price;
    private String description;
    private String image;
    @ManyToOne
    @JoinColumn(name ="discount_id" )
    private Discount discount;
}
