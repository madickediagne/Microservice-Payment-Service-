package com.transport.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FareCalculationResponse {
    private String lineId;
    private String lineName;
    private String passengerType;
    private BigDecimal calculatedFare;
    private String currency;
}
