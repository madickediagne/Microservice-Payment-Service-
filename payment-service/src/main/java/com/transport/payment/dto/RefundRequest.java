package com.transport.payment.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefundRequest {

    @NotBlank(message = "L'identifiant de la transaction à rembourser est obligatoire")
    private String originalTransactionRef;

    @NotBlank(message = "Le motif du remboursement est obligatoire")
    private String reason;

    @NotNull(message = "Le montant est obligatoire")
    @DecimalMin(value = "1.00", message = "Le montant doit être supérieur à 0")
    private BigDecimal refundAmount;
}
