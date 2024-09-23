package com.iot.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "TemperatureHumidity")
public class TemperatureHumidity extends CommonEntity{
    private Integer temperature;
    private Integer humidity;

}
