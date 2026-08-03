package com.transport.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RidePaymentResponse {
    private String transactionRef;
    private String userId;
    private String cardId;
    private String rideId;
    private BigDecimal debitedAmount;
    private BigDecimal remainingBalance;
    private String status; // SUCCESS, INSUFFICIENT_FUNDS, FAILED
    private String message;
    private LocalDateTime timestamp;
}
