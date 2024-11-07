package com.iot.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String user_name;
    private String firstname;
    private String lastname;
    private String phone_number;
    private Date date_of_birth;
    private String email;
    private String address;
    private String role;
    private String status;
    private String images_src;
    private String company;
}
