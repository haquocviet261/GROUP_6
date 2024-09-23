package com.iot.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iot.common.constant.DeviceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Device")
public class Device extends CommonEntity{
    private String name;
    private String mac_address;
    private Long user_id;
}
