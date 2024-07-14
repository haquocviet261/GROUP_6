package com.petshop.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BmiResponse {
    private String weight;
    private  String height;
    private String age ;
    private String gender;
    private Long user_id;
}
