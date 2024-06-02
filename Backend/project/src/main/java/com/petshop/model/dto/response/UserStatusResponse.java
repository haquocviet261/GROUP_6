package com.petshop.model.dto.response;

import com.petshop.model.entity.OnlineStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserStatusResponse {
    private Long user_id;
    private String user_name;
    private String first_name;
    private String last_name;
    private String image_src;
    private Long online_status;
}
