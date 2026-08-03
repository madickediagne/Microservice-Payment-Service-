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
public class FareCalculationRequest {
    private String lineId;
    private String passengerType; // REGULAR, STUDENT, SENIOR, DISABILITY
}
