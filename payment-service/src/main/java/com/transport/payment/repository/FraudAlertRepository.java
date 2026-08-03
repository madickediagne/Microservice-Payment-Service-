package com.transport.payment.repository;

import com.transport.payment.model.FraudAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FraudAlertRepository extends JpaRepository<FraudAlert, Long> {
    List<FraudAlert> findByUserId(String userId);
    List<FraudAlert> findByResolvedFalseOrderByTimestampDesc();
    List<FraudAlert> findTop50ByOrderByTimestampDesc();
}
