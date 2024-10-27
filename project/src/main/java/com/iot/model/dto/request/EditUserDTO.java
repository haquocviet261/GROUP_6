package com.iot.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditUserDTO {
    private String firstname;
    private String lastname;
    private String phone_number;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date_of_birth;
    private String address;
    private String images_src;
}
