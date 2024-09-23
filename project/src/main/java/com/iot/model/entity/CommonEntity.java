package com.iot.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
@MappedSuperclass
@Data
public class CommonEntity {
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
}
