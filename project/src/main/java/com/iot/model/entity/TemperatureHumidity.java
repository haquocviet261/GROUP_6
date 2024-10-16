package com.iot.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TemperatureHumidity")
public class TemperatureHumidity extends CommonEntity{
    @Column(name = "temperature")
    private Integer temperature;
    @Column(name = "humidity")
    private Integer humidity;
}
