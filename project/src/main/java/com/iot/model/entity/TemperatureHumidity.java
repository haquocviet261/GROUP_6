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
@Table(name = "TemperatureHumidity")
public class TemperatureHumidity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "temperature")
    private Double temperature;
    @Column(name = "humidity")
    private Double humidity;
    @Column(name = "company_id")
    private Integer companyId;
    @Column(name = "created_at")
    private Date created_at;

    public TemperatureHumidity(Double temperature, Double humidity, Integer companyId, Date created_at) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.companyId = companyId;
        this.created_at = created_at;
    }
}
