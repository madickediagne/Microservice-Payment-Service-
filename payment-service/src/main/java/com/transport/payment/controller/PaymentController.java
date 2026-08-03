package com.transport.payment.controller;

import com.transport.payment.dto.*;
import com.transport.payment.model.PaymentTransaction;
import com.transport.payment.service.PaymentService;
import com.transport.payment.service.WavePaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Gestion des Paiements", description = "Endpoints pour la recharge Wave, le débit automatique de trajet bus, l'historique et les remboursements")
public class PaymentController {

    private final WavePaymentService wavePaymentService;
    private final PaymentService paymentService;

    @PostMapping("/wave/recharge")
    @Operation(summary = "Recharger le portefeuille via Wave (Simulation)", description = "Simule un paiement Wave Mobile Money et crédite immédiatement le portefeuille de l'utilisateur.")
    public ResponseEntity<WaveRechargeResponse> rechargeViaWave(@Valid @RequestBody WaveRechargeRequest request) {
        WaveRechargeResponse response = wavePaymentService.processWaveRecharge(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/process-ride")
    @Operation(summary = "Paiement automatique d'un trajet de bus", description = "Appelé par le Transport Service (Groupe 2) lors de la validation NFC/QR Code du passager. Vérifie le solde et débite le montant du trajet.")
    public ResponseEntity<RidePaymentResponse> processRidePayment(@Valid @RequestBody RidePaymentRequest request) {
        RidePaymentResponse response = paymentService.processRidePayment(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refund")
    @Operation(summary = "Gestion des remboursements (Bonus)", description = "Permet de rembourser une transaction originale à l'utilisateur.")
    public ResponseEntity<PaymentTransaction> refundTransaction(@Valid @RequestBody RefundRequest request) {
        PaymentTransaction refund = paymentService.refundTransaction(request);
        return new ResponseEntity<>(refund, HttpStatus.CREATED);
    }

    @GetMapping("/history/{userId}")
    @Operation(summary = "Historique des paiements d'un utilisateur", description = "Retourne la liste de toutes les recharges et débits effectués par un utilisateur.")
    public ResponseEntity<List<PaymentTransaction>> getUserHistory(@PathVariable String userId) {
        return ResponseEntity.ok(paymentService.getUserTransactionHistory(userId));
    }

    @GetMapping("/recent")
    @Operation(summary = "Consulter les transactions récentes globales", description = "Retourne les 20 dernières transactions enregistrées sur la plateforme.")
    public ResponseEntity<List<PaymentTransaction>> getRecentTransactions() {
        return ResponseEntity.ok(paymentService.getRecentTransactions());
    }
}
