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
public class WaveRechargeResponse {
    private String transactionRef;
    private String userId;
    private String cardId;
    private BigDecimal amount;
    private BigDecimal newBalance;
    private String currency;
    private String status; // SUCCESS, FAILED
    private String wavePaymentUrl; // URL de simulation Wave
    private String message;
    private LocalDateTime timestamp;
}
