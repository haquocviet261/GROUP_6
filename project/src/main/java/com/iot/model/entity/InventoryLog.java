package com.iot.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "InventoryLog")
public class InventoryLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "food_item_id")
    private Integer foodItemId;
    @Column(name = "closing_quantity")
    private Double closingQuantity;
    @Column(name = "unit")
    private String unit;
    @Column(name = "created_at")
    private Date created_at;
    @Column(name = "company_id")
    private Integer companyId;
    @Column(name = "food_name")
    private String foodName;
    @Column(name = "added_quantity")
    private Double addedQuantity;
    @Column(name = "consumed_quantity")
    private Double consumedQuantity;

}
