package com.petshop.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserStatusResponse {
    private String user_name;
    private String full_name;
    private String image_src;
    private Long status;
}
