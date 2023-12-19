package com.petshop.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "sub_category_id")
    private SubCategory subCategory;
    private String product_name;
    private int quantity;
    private double price;
    private String description;
    @Column(name = "product_image")
    private String image;
    @ManyToOne
    @JoinColumn(name ="discount_id")
    private Discount discount;
    @JsonIgnore
    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    List<Item> items;
}
