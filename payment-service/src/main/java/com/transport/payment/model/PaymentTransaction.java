package com.transport.payment.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_ref", nullable = false, unique = true)
    private String transactionRef;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "card_id", nullable = false)
    private String cardId;

    @Column(nullable = false, length = 30)
    private String type; // RECHARGE_WAVE, RIDE_DEBIT, REFUND

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "balance_after", nullable = false, precision = 10, scale = 2)
    private BigDecimal balanceAfter;

    @Column(nullable = false, length = 10)
    private String currency;

    @Column(nullable = false, length = 20)
    private String status; // SUCCESS, FAILED, PENDING

    @Column(name = "payment_method", length = 50)
    private String paymentMethod; // WAVE, WALLET

    @Column(name = "ride_id", length = 100)
    private String rideId;

    @Column(name = "line_id", length = 100)
    private String lineId;

    @Column(name = "bus_id", length = 100)
    private String busId;

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @PrePersist
    protected void onCreate() {
        if (this.timestamp == null) {
            this.timestamp = LocalDateTime.now();
        }
        if (this.currency == null) {
            this.currency = "XOF";
        }
    }
}
