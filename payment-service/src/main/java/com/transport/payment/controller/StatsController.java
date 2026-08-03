package com.transport.payment.controller;

import com.transport.payment.dto.StatsResponse;
import com.transport.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
@Tag(name = "Statistiques & Tableau de bord (Bonus)", description = "Endpoints d'agrégation des métriques financières et d'utilisation")
public class StatsController {

    private final PaymentService paymentService;

    @GetMapping("/dashboard")
    @Operation(summary = "Obtenir les statistiques financières globales", description = "Retourne le total rechargé, le total débité, le nombre de transactions et le nombre d'alertes.")
    public ResponseEntity<StatsResponse> getDashboardStats() {
        return ResponseEntity.ok(paymentService.getFinancialStats());
    }
}
