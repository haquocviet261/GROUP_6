package com.iot.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iot.common.constant.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "Bmi")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bmi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bmi_id;
    private String weight;
    private  String height;
    private String age ;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @JsonIgnore
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
}
