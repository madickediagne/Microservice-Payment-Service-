package com.transport.payment.service;

import com.transport.payment.dto.WaveRechargeRequest;
import com.transport.payment.dto.WaveRechargeResponse;
import com.transport.payment.model.PaymentTransaction;
import com.transport.payment.model.Wallet;
import com.transport.payment.repository.PaymentTransactionRepository;
import com.transport.payment.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WavePaymentService {

    private final WalletRepository walletRepository;
    private final PaymentTransactionRepository paymentTransactionRepository;

    @Value("${wave.simulation.merchant-name:AbuDhabi-SmartTransport-IPM2026}")
    private String merchantName;

    @Transactional
    public WaveRechargeResponse processWaveRecharge(WaveRechargeRequest request) {
        String userId = request.getUserId();
        String cardId = request.getCardId() != null ? request.getCardId() : "CARD_" + userId;
        BigDecimal amount = request.getAmount();

        // Récupération ou création automatique du portefeuille si inexistant
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseGet(() -> Wallet.builder()
                        .userId(userId)
                        .cardId(cardId)
                        .balance(BigDecimal.ZERO)
                        .currency("XOF")
                        .status("ACTIVE")
                        .build());

        // Crédit du solde
        BigDecimal newBalance = wallet.getBalance().add(amount);
        wallet.setBalance(newBalance);
        walletRepository.save(wallet);

        // Enregistrement de la transaction de recharge
        String txRef = "WAVE_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        PaymentTransaction tx = PaymentTransaction.builder()
                .transactionRef(txRef)
                .userId(userId)
                .cardId(wallet.getCardId())
                .type("RECHARGE_WAVE")
                .amount(amount)
                .balanceAfter(newBalance)
                .currency("XOF")
                .status("SUCCESS")
                .paymentMethod("WAVE")
                .description("Recharge portefeuille via Wave Mobile Money (N° " + request.getPhoneNumber() + ")")
                .timestamp(LocalDateTime.now())
                .build();
        paymentTransactionRepository.save(tx);

        return WaveRechargeResponse.builder()
                .transactionRef(txRef)
                .userId(userId)
                .cardId(wallet.getCardId())
                .amount(amount)
                .newBalance(newBalance)
                .currency("XOF")
                .status("SUCCESS")
                .wavePaymentUrl("https://wave.com/checkout/simulate/" + txRef)
                .message("Recharge Wave effectuée avec succès. Nouveau solde : " + newBalance + " XOF")
                .timestamp(LocalDateTime.now())
                .build();
    }
}
