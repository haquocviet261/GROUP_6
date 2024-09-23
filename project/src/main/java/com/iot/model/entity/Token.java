package com.iot.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Token")
public class Token extends CommonEntity{
    @Column(name = "token")
    public String token;

    public boolean revoked;

    public boolean expired;

    public Long user_id;
}