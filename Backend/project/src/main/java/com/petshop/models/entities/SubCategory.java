package com.petshop.models.entities;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
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
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Categories category;
    private String sub_category_name;
    @Transient
    public Long getCategory_id() {
        return (category != null) ? category.getCategory_id() : null;
    }
    private Date date_created;
    private Date date_modified;
    @JsonIgnore
    @OneToMany(mappedBy = "subCategory")
    private List<Product> products;

}
