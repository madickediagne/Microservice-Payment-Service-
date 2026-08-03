package com.transport.payment.controller;

import com.transport.payment.model.FraudAlert;
import com.transport.payment.service.FraudDetectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/fraud")
@RequiredArgsConstructor
@Tag(name = "Détection de Fraude (Bonus)", description = "Endpoints de surveillance et gestion des alertes de sécurité et fraudes")
public class FraudController {

    private final FraudDetectionService fraudDetectionService;

    @GetMapping("/alerts")
    @Operation(summary = "Consulter les alertes de fraude en cours", description = "Retourne la liste des alertes non résolues (débits répétitifs, tentatives de solde insuffisant, etc.).")
    public ResponseEntity<List<FraudAlert>> getActiveAlerts() {
        return ResponseEntity.ok(fraudDetectionService.getUnresolvedAlerts());
    }

    @GetMapping("/all")
    @Operation(summary = "Consulter toutes les alertes de fraude", description = "Retourne l'historique complet des alertes générées.")
    public ResponseEntity<List<FraudAlert>> getAllAlerts() {
        return ResponseEntity.ok(fraudDetectionService.getAllAlerts());
    }

    @PutMapping("/resolve/{alertId}")
    @Operation(summary = "Résoudre une alerte de fraude", description = "Marque une alerte de sécurité comme traitée.")
    public ResponseEntity<FraudAlert> resolveAlert(@PathVariable Long alertId) {
        return ResponseEntity.ok(fraudDetectionService.resolveAlert(alertId));
    }
}
