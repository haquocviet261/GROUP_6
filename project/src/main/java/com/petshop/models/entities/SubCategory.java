package com.petshop.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sub_categories")
public class SubCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sub_category_id;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Categories category;
    @OneToMany(mappedBy = "subCategory")
    private List<Product> products;

}
