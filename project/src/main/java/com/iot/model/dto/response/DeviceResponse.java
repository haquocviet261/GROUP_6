package com.iot.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceResponse {
    private Long deviceId;
    private List<Long> listFoodItemId;
}
