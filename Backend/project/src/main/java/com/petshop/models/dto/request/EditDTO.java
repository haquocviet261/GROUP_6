package com.petshop.models.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditDTO {
    private String firstname;
    private String lastname;
    private String address;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateofbirth;
}
