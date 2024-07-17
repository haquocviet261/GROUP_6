package com.petshop.model.dto.response;

import com.petshop.common.constant.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BmiResponse {
    private String weight;
    private String height;
    private String age ;
    private Gender gender;
    private Long user_id;
}
