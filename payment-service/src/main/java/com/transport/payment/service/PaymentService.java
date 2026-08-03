package com.transport.payment.service;

import com.transport.payment.dto.*;
import com.transport.payment.exception.InsufficientBalanceException;
import com.transport.payment.exception.PaymentException;
import com.transport.payment.exception.ResourceNotFoundException;
import com.transport.payment.model.PaymentTransaction;
import com.transport.payment.model.Wallet;
import com.transport.payment.repository.PaymentTransactionRepository;
import com.transport.payment.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final WalletRepository walletRepository;
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final FareCalculatorService fareCalculatorService;
    private final FraudDetectionService fraudDetectionService;

    /**
     * Traitement du paiement automatique d'un trajet de bus.
     * Cette méthode est appelée par le Transport Service lors de la validation du passager (NFC / QR Code).
     */
    @Transactional
    public RidePaymentResponse processRidePayment(RidePaymentRequest request) {
        String cardId = request.getCardId();
        
        // Recherche du portefeuille par cardId ou userId (création automatique avec solde 0 si première utilisation)
        Wallet wallet = walletRepository.findByCardId(cardId)
                .or(() -> request.getUserId() != null ? walletRepository.findByUserId(request.getUserId()) : java.util.Optional.empty())
                .orElseGet(() -> walletRepository.save(Wallet.builder()
                        .userId(request.getUserId() != null ? request.getUserId() : cardId)
                        .cardId(cardId)
                        .balance(BigDecimal.ZERO)
                        .currency("XOF")
                        .status("ACTIVE")
                        .build()));

        if (!"ACTIVE".equalsIgnoreCase(wallet.getStatus())) {
            throw new PaymentException("Le portefeuille lié à la carte " + cardId + " est bloqué ou inactif.");
        }

        // Calcul ou détermination du tarif du trajet
        BigDecimal fareAmount;
        if (request.getFixedFare() != null && request.getFixedFare().compareTo(BigDecimal.ZERO) > 0) {
            fareAmount = request.getFixedFare();
        } else {
            FareCalculationRequest fareReq = FareCalculationRequest.builder()
                    .lineId(request.getLineId())
                    .passengerType(request.getPassengerType() != null ? request.getPassengerType() : "REGULAR")
                    .build();
            fareAmount = fareCalculatorService.calculateFare(fareReq).getCalculatedFare();
        }

        // Analyse anti-fraude préventive
        fraudDetectionService.analyzeRidePayment(wallet.getUserId(), cardId, fareAmount);

        // Vérification du solde du portefeuille
        if (wallet.getBalance().compareTo(fareAmount) < 0) {
            fraudDetectionService.logInsufficientFundsAttempt(wallet.getUserId(), cardId, fareAmount, wallet.getBalance());
            
            // Enregistrement d'une transaction échouée
            String failTxRef = "TX_FAIL_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            PaymentTransaction failedTx = PaymentTransaction.builder()
                    .transactionRef(failTxRef)
                    .userId(wallet.getUserId())
                    .cardId(cardId)
                    .type("RIDE_DEBIT")
                    .amount(fareAmount)
                    .balanceAfter(wallet.getBalance())
                    .currency("XOF")
                    .status("FAILED")
                    .paymentMethod("WALLET")
                    .rideId(request.getRideId())
                    .lineId(request.getLineId())
                    .busId(request.getBusId())
                    .description("Échec validation bus : Solde insuffisant (" + wallet.getBalance() + " XOF)")
                    .timestamp(LocalDateTime.now())
                    .build();
            paymentTransactionRepository.save(failedTx);

            throw new InsufficientBalanceException("Solde insuffisant (" + wallet.getBalance() + " XOF). Tarif requis : " + fareAmount + " XOF");
        }

        // Débit effectif du montant
        BigDecimal remainingBalance = wallet.getBalance().subtract(fareAmount);
        wallet.setBalance(remainingBalance);
        walletRepository.save(wallet);

        // Enregistrement de la transaction réussie
        String txRef = "RIDE_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        PaymentTransaction tx = PaymentTransaction.builder()
                .transactionRef(txRef)
                .userId(wallet.getUserId())
                .cardId(cardId)
                .type("RIDE_DEBIT")
                .amount(fareAmount)
                .balanceAfter(remainingBalance)
                .currency("XOF")
                .status("SUCCESS")
                .paymentMethod("WALLET")
                .rideId(request.getRideId())
                .lineId(request.getLineId())
                .busId(request.getBusId())
                .description("Paiement trajet Ligne " + request.getLineId() + " / Bus " + (request.getBusId() != null ? request.getBusId() : "N/A"))
                .timestamp(LocalDateTime.now())
                .build();
        paymentTransactionRepository.save(tx);

        return RidePaymentResponse.builder()
                .transactionRef(txRef)
                .userId(wallet.getUserId())
                .cardId(cardId)
                .rideId(request.getRideId())
                .debitedAmount(fareAmount)
                .remainingBalance(remainingBalance)
                .status("SUCCESS")
                .message("Paiement validé avec succès. Nouveau solde : " + remainingBalance + " XOF")
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Remboursement d'un trajet (Bonus).
     */
    @Transactional
    public PaymentTransaction refundTransaction(RefundRequest request) {
        PaymentTransaction originalTx = paymentTransactionRepository.findByTransactionRef(request.getOriginalTransactionRef())
                .orElseThrow(() -> new ResourceNotFoundException("Transaction originale introuvable : " + request.getOriginalTransactionRef()));

        Wallet wallet = walletRepository.findByUserId(originalTx.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Portefeuille introuvable pour l'utilisateur : " + originalTx.getUserId()));

        BigDecimal newBalance = wallet.getBalance().add(request.getRefundAmount());
        wallet.setBalance(newBalance);
        walletRepository.save(wallet);

        String refundRef = "REFUND_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        PaymentTransaction refundTx = PaymentTransaction.builder()
                .transactionRef(refundRef)
                .userId(originalTx.getUserId())
                .cardId(originalTx.getCardId())
                .type("REFUND")
                .amount(request.getRefundAmount())
                .balanceAfter(newBalance)
                .currency("XOF")
                .status("SUCCESS")
                .paymentMethod("WALLET")
                .description("Remboursement transaction " + originalTx.getTransactionRef() + " - Motif: " + request.getReason())
                .timestamp(LocalDateTime.now())
                .build();

        return paymentTransactionRepository.save(refundTx);
    }

    public WalletResponse getWalletByUserId(String userId) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .or(() -> walletRepository.findByCardId(userId))
                .orElseGet(() -> walletRepository.save(Wallet.builder()
                        .userId(userId)
                        .cardId("CARD_" + userId)
                        .balance(BigDecimal.ZERO)
                        .currency("XOF")
                        .status("ACTIVE")
                        .build()));

        return WalletResponse.builder()
                .id(wallet.getId())
                .userId(wallet.getUserId())
                .cardId(wallet.getCardId())
                .balance(wallet.getBalance())
                .currency(wallet.getCurrency())
                .status(wallet.getStatus())
                .updatedAt(wallet.getUpdatedAt())
                .build();
    }

    public List<PaymentTransaction> getUserTransactionHistory(String userId) {
        return paymentTransactionRepository.findByUserIdOrderByTimestampDesc(userId);
    }

    public List<PaymentTransaction> getRecentTransactions() {
        return paymentTransactionRepository.findTop20ByOrderByTimestampDesc();
    }

    public StatsResponse getFinancialStats() {
        BigDecimal totalRecharged = paymentTransactionRepository.sumAmountByStatusAndType("RECHARGE_WAVE");
        BigDecimal totalDebited = paymentTransactionRepository.sumAmountByStatusAndType("RIDE_DEBIT");
        Long totalCount = paymentTransactionRepository.count();
        Long successCount = paymentTransactionRepository.countByStatus("SUCCESS");
        Long failedCount = paymentTransactionRepository.countByStatus("FAILED");
        Long activeAlerts = (long) fraudDetectionService.getUnresolvedAlerts().size();

        return StatsResponse.builder()
                .totalRecharged(totalRecharged != null ? totalRecharged : BigDecimal.ZERO)
                .totalDebited(totalDebited != null ? totalDebited : BigDecimal.ZERO)
                .totalTransactionsCount(totalCount)
                .successCount(successCount)
                .failedCount(failedCount)
                .activeAlertsCount(activeAlerts)
                .build();
    }
}
