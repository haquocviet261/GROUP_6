package com.iot.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Company")
public class Company extends CommonEntity {
    @Column(name = "name")
    private String name;
    @Column(name = "user_id")
    private Long user_id;
}
