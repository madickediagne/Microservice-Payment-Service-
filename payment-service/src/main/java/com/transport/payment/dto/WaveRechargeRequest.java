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
public class WaveRechargeRequest {

    @NotBlank(message = "L'identifiant de l'utilisateur est obligatoire")
    private String userId;

    private String cardId;

    @NotBlank(message = "Le numéro de téléphone Wave est obligatoire")
    private String phoneNumber;

    @NotNull(message = "Le montant est obligatoire")
    @DecimalMin(value = "100.00", message = "Le montant minimum de recharge est de 100 XOF")
    private BigDecimal amount;

    private String otpCode; // Simulation de validation OTP fictive
}
