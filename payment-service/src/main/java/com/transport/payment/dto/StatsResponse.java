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
public class StatsResponse {
    private BigDecimal totalRecharged;
    private BigDecimal totalDebited;
    private Long totalTransactionsCount;
    private Long successCount;
    private Long failedCount;
    private Long activeAlertsCount;
}
