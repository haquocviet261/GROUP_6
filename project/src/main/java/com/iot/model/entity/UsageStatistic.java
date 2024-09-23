package com.iot.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "UsageStatistic")
public class UsageStatistic extends CommonEntity{
    private Integer food_item_id;
    private Date date;
    private Integer used_quantity;
    private Integer remaining_quantity;
    private Integer unit;
}
