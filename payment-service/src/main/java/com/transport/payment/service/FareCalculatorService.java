package com.transport.payment.service;

import com.transport.payment.dto.FareCalculationRequest;
import com.transport.payment.dto.FareCalculationResponse;
import com.transport.payment.model.FareRule;
import com.transport.payment.repository.FareRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FareCalculatorService {

    private final FareRuleRepository fareRuleRepository;

    public FareCalculationResponse calculateFare(FareCalculationRequest request) {
        String passengerType = request.getPassengerType() != null ? request.getPassengerType().toUpperCase() : "REGULAR";
        String lineId = request.getLineId() != null ? request.getLineId() : "LINE_01";

        FareRule rule = fareRuleRepository.findByLineIdAndPassengerTypeAndActiveTrue(lineId, passengerType)
                .orElseGet(() -> fareRuleRepository.findByLineIdAndPassengerTypeAndActiveTrue(lineId, "REGULAR")
                        .orElse(FareRule.builder()
                                .lineId(lineId)
                                .lineName("Ligne " + lineId)
                                .passengerType(passengerType)
                                .baseFare(new BigDecimal("350.00"))
                                .active(true)
                                .build()));

        return FareCalculationResponse.builder()
                .lineId(rule.getLineId())
                .lineName(rule.getLineName())
                .passengerType(passengerType)
                .calculatedFare(rule.getBaseFare())
                .currency("XOF")
                .build();
    }

    public List<FareRule> getAllActiveFareRules() {
        return fareRuleRepository.findByActiveTrue();
    }
}
