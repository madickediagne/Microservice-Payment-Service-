package com.transport.payment;

import com.transport.payment.dto.RidePaymentRequest;
import com.transport.payment.dto.RidePaymentResponse;
import com.transport.payment.dto.WaveRechargeRequest;
import com.transport.payment.dto.WaveRechargeResponse;
import com.transport.payment.exception.InsufficientBalanceException;
import com.transport.payment.model.Wallet;
import com.transport.payment.repository.WalletRepository;
import com.transport.payment.service.PaymentService;
import com.transport.payment.service.WavePaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PaymentServiceTest {

    @Autowired
    private WavePaymentService wavePaymentService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private WalletRepository walletRepository;

    private final String testUserId = "TEST_USER_99";
    private final String testCardId = "TEST_CARD_99";

    @BeforeEach
    void setUp() {
        walletRepository.findByUserId(testUserId).ifPresent(walletRepository::delete);
    }

    @Test
    void testWaveRechargeSuccess() {
        WaveRechargeRequest rechargeRequest = WaveRechargeRequest.builder()
                .userId(testUserId)
                .cardId(testCardId)
                .phoneNumber("+221770000000")
                .amount(new BigDecimal("5000.00"))
                .build();

        WaveRechargeResponse response = wavePaymentService.processWaveRecharge(rechargeRequest);

        assertNotNull(response);
        assertEquals("SUCCESS", response.getStatus());
        assertEquals(new BigDecimal("5000.00"), response.getNewBalance());

        Wallet wallet = walletRepository.findByUserId(testUserId).orElse(null);
        assertNotNull(wallet);
        assertEquals(new BigDecimal("5000.00"), wallet.getBalance());
    }

    @Test
    void testRidePaymentDebitSuccess() {
        // Recharge préalable
        testWaveRechargeSuccess();

        RidePaymentRequest rideRequest = RidePaymentRequest.builder()
                .userId(testUserId)
                .cardId(testCardId)
                .rideId("RIDE_001")
                .lineId("LINE_01")
                .fixedFare(new BigDecimal("500.00"))
                .build();

        RidePaymentResponse rideResponse = paymentService.processRidePayment(rideRequest);

        assertNotNull(rideResponse);
        assertEquals("SUCCESS", rideResponse.getStatus());
        assertEquals(new BigDecimal("500.00"), rideResponse.getDebitedAmount());
        assertEquals(new BigDecimal("4500.00"), rideResponse.getRemainingBalance());
    }

    @Test
    void testRidePaymentInsufficientBalanceException() {
        // Solde initial nul
        RidePaymentRequest rideRequest = RidePaymentRequest.builder()
                .userId("POOR_USER")
                .cardId("POOR_CARD")
                .rideId("RIDE_002")
                .lineId("LINE_01")
                .fixedFare(new BigDecimal("500.00"))
                .build();

        assertThrows(InsufficientBalanceException.class, () -> {
            paymentService.processRidePayment(rideRequest);
        });
    }
}
