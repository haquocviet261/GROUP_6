package com.petshop.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "discount")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long discount_id;
    private String discount_name;
    private double  discount_value;
    private Date start_date;
    private Date end_date;
    @OneToMany(mappedBy = "discount",cascade = CascadeType.ALL)
    List<Product> products;
}
