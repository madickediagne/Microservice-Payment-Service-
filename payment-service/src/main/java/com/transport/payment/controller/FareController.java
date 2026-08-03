package com.transport.payment.controller;

import com.transport.payment.dto.FareCalculationRequest;
import com.transport.payment.dto.FareCalculationResponse;
import com.transport.payment.model.FareRule;
import com.transport.payment.service.FareCalculatorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/fares")
@RequiredArgsConstructor
@Tag(name = "Calculateur des Tarifs", description = "Endpoints de gestion et calcul automatique des tarifs par ligne et profil passager")
public class FareController {

    private final FareCalculatorService fareCalculatorService;

    @PostMapping("/calculate")
    @Operation(summary = "Calculer le tarif d'un trajet", description = "Retourne le montant exact du tarif en fonction de la ligne et de la catégorie de passager.")
    public ResponseEntity<FareCalculationResponse> calculateFare(@RequestBody FareCalculationRequest request) {
        return ResponseEntity.ok(fareCalculatorService.calculateFare(request));
    }

    @GetMapping("/rules")
    @Operation(summary = "Consulter les règles tarifaires", description = "Retourne la grille tarifaire complète active de toutes les lignes de bus.")
    public ResponseEntity<List<FareRule>> getFareRules() {
        return ResponseEntity.ok(fareCalculatorService.getAllActiveFareRules());
    }
}
