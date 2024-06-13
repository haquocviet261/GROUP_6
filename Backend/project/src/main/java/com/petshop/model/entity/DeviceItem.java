package com.petshop.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "DeviceItem")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long device_item_id;
    private String device_name;
    private String mac_address;
    @JsonIgnore
    @JoinColumn(name = "device_id")
    @ManyToOne
    private Device device;
    @OneToMany(mappedBy = "deviceItem",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonIgnore
    private List<FoodItem> foodItemList;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;
}
