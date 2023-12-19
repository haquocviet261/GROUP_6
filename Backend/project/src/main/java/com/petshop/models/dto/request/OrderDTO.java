package com.petshop.models.dto.request;

import com.petshop.models.entities.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {
    private String fullname;
    private Date order_date;
    private String phone_number;
    private int status;
    private String address;
    private List<CartItemDTO> items;

}
