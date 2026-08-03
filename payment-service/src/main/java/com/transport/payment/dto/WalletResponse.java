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
public class WalletResponse {
    private Long id;
    private String userId;
    private String cardId;
    private BigDecimal balance;
    private String currency;
    private String status;
    private LocalDateTime updatedAt;
}
