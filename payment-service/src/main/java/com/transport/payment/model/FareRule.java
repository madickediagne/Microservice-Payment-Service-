package com.transport.payment.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "fare_rules")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FareRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "line_id", nullable = false)
    private String lineId;

    @Column(name = "line_name", nullable = false)
    private String lineName;

    @Column(name = "passenger_type", nullable = false)
    private String passengerType; // REGULAR, STUDENT, SENIOR, DISABILITY

    @Column(name = "base_fare", nullable = false, precision = 10, scale = 2)
    private BigDecimal baseFare;

    @Column(nullable = false)
    private Boolean active;
}
