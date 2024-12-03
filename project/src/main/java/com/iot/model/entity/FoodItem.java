package com.iot.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "FoodItem")
public class FoodItem{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "created_at")
    private Date created_at;

    @Column(name = "updated_at")
    private Date updated_at;

    @Column(name = "deleted_at")
    private Date deleted_at;

    @Column(name = "updated_by")
    private String updated_by;

    @Column(name = "food_id")
    private Integer food_id;

    @Column(name = "name")
    private String name;

    @Column(name = "type_unit")
    private String type_unit;

    @Column(name = "expired_date")
    private Integer expired_date;

    @Column(name = "quantity")
    private Double quantity;

    @Column(name = "company_id")
    private Integer companyId;

    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "last_notified_at")
    private Date lastNotifiedAt;            //thời điểm gần nhất mà thông báo được gửi liên quan đến Fooditem này

    @Column(name = "days_before_expired_notified")
    private Integer daysBeforeExpiredNotified;          //số ngày hết hạn còn lại

    @Column(name = "last_checked_expiration_date")
    private Date lastCheckedExpirationDate;             //ngày hết hạn mà hệ thống đã kiểm tra gần nhất

    @Column(name = "last_increase_time")
    private Date lastIncreaseTime;

    @Column(name = "last_increase_weight")
    private Double lastIncreaseWeight;

    @Column(name = "is_low_stock", nullable = false)
    private Boolean isLowStock;

    @PrePersist
    public void prePersist() {
        created_at = new Date();
        updated_at = new Date();
    }
}
