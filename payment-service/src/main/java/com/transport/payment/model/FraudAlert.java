package com.transport.payment.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "fraud_alerts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FraudAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "card_id", nullable = false)
    private String cardId;

    @Column(name = "fraud_type", nullable = false)
    private String fraudType; // RAPID_TRANSACTIONS, INSUFFICIENT_FUNDS_REPEATED, UNUSUAL_AMOUNT

    @Column(name = "risk_level", nullable = false)
    private String riskLevel; // LOW, MEDIUM, HIGH, CRITICAL

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private Boolean resolved;

    @PrePersist
    protected void onCreate() {
        if (this.timestamp == null) {
            this.timestamp = LocalDateTime.now();
        }
        if (this.resolved == null) {
            this.resolved = false;
        }
    }
}
