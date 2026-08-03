package com.transport.payment.repository;

import com.transport.payment.model.FareRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FareRuleRepository extends JpaRepository<FareRule, Long> {
    Optional<FareRule> findByLineIdAndPassengerTypeAndActiveTrue(String lineId, String passengerType);
    List<FareRule> findByLineIdAndActiveTrue(String lineId);
    List<FareRule> findByActiveTrue();
}
