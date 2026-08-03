package com.transport.payment.service;

import com.transport.payment.model.FraudAlert;
import com.transport.payment.model.PaymentTransaction;
import com.transport.payment.repository.FraudAlertRepository;
import com.transport.payment.repository.PaymentTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FraudDetectionService {

    private final FraudAlertRepository fraudAlertRepository;
    private final PaymentTransactionRepository paymentTransactionRepository;

    /**
     * Analyse les transactions récentes pour détecter des anomalies comportementales.
     */
    public void analyzeRidePayment(String userId, String cardId, BigDecimal fareAmount) {
        LocalDateTime twoMinutesAgo = LocalDateTime.now().minusMinutes(2);
        List<PaymentTransaction> recentTransactions = paymentTransactionRepository
                .findByUserIdAndTimestampAfter(userId, twoMinutesAgo);

        // Règle 1: Débit trop rapide (plus de 3 tentatives en moins de 2 minutes)
        if (recentTransactions.size() >= 3) {
            FraudAlert alert = FraudAlert.builder()
                    .userId(userId)
                    .cardId(cardId)
                    .fraudType("RAPID_TRANSACTIONS")
                    .riskLevel("HIGH")
                    .description("Plus de 3 tentatives de validation en moins de 2 minutes pour la carte " + cardId)
                    .resolved(false)
                    .build();
            fraudAlertRepository.save(alert);
        }

        // Règle 2: Montant anormalement élevé pour un trajet bus (> 2000 XOF)
        if (fareAmount != null && fareAmount.compareTo(new BigDecimal("2000.00")) > 0) {
            FraudAlert alert = FraudAlert.builder()
                    .userId(userId)
                    .cardId(cardId)
                    .fraudType("UNUSUAL_AMOUNT")
                    .riskLevel("MEDIUM")
                    .description("Montant de débit bus inhabituel (" + fareAmount + " XOF)")
                    .resolved(false)
                    .build();
            fraudAlertRepository.save(alert);
        }
    }

    public void logInsufficientFundsAttempt(String userId, String cardId, BigDecimal requiredAmount, BigDecimal currentBalance) {
        FraudAlert alert = FraudAlert.builder()
                .userId(userId)
                .cardId(cardId)
                .fraudType("INSUFFICIENT_FUNDS_REPEATED")
                .riskLevel("LOW")
                .description("Échec de débit : Solde actuel de " + currentBalance + " XOF inférieur au tarif requis de " + requiredAmount + " XOF")
                .resolved(false)
                .build();
        fraudAlertRepository.save(alert);
    }

    public List<FraudAlert> getUnresolvedAlerts() {
        return fraudAlertRepository.findByResolvedFalseOrderByTimestampDesc();
    }

    public List<FraudAlert> getAllAlerts() {
        return fraudAlertRepository.findTop50ByOrderByTimestampDesc();
    }

    public FraudAlert resolveAlert(Long alertId) {
        FraudAlert alert = fraudAlertRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Alerte introuvable id: " + alertId));
        alert.setResolved(true);
        return fraudAlertRepository.save(alert);
    }
}
