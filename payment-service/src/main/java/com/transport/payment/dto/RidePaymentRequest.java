package com.transport.payment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RidePaymentRequest {

    private String userId;

    @NotBlank(message = "L'identifiant de la carte ou QR Code est obligatoire")
    private String cardId;

    @NotBlank(message = "L'identifiant du trajet est obligatoire")
    private String rideId;

    @NotBlank(message = "L'identifiant de la ligne est obligatoire")
    private String lineId;

    private String busId;

    private String passengerType; // REGULAR, STUDENT, SENIOR, etc.

    private BigDecimal fixedFare; // Facultatif: si fourni par le Transport Service, sinon calculé
}
