package com.petshop.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BmiRequest {
    private String weight;
    private  String height;
    private String age ;
    private String gender;
    private Long user_id;
}
