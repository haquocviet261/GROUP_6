package com.iot.model.dto.response;

import lombok.*;

@Setter
@Getter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConsumeQuantity {
    private String foodName;
    private Double consumeQuantity;

    public void addConsumeQuantity(Double quantity) {
        if (quantity != null) {
            this.consumeQuantity += quantity;
        }
    }

}
