package com.transport.payment.controller;

import com.transport.payment.dto.WalletResponse;
import com.transport.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
@Tag(name = "Portefeuille & Solde", description = "Endpoints de consultation du portefeuille électronique utilisateur")
public class WalletController {

    private final PaymentService paymentService;

    @GetMapping("/{userId}")
    @Operation(summary = "Consulter le portefeuille et le solde d'un utilisateur", description = "Retourne les détails du portefeuille, le solde actuel et le statut de la carte.")
    public ResponseEntity<WalletResponse> getWalletInfo(@PathVariable String userId) {
        return ResponseEntity.ok(paymentService.getWalletByUserId(userId));
    }
}
