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
@Table(name = "products")
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
    @Column(name = "product_image")
    private String image;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="discount_id",nullable = true)
    private Discount discount;
}
