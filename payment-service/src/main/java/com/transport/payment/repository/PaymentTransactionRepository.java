package com.transport.payment.repository;

import com.transport.payment.model.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {

    Optional<PaymentTransaction> findByTransactionRef(String transactionRef);

    List<PaymentTransaction> findByUserIdOrderByTimestampDesc(String userId);

    List<PaymentTransaction> findByCardIdOrderByTimestampDesc(String cardId);

    List<PaymentTransaction> findTop20ByOrderByTimestampDesc();

    List<PaymentTransaction> findByUserIdAndTimestampAfter(String userId, LocalDateTime since);

    @Query("SELECT SUM(t.amount) FROM PaymentTransaction t WHERE t.status = 'SUCCESS' AND t.type = :type")
    BigDecimal sumAmountByStatusAndType(@Param("type") String type);

    @Query("SELECT COUNT(t) FROM PaymentTransaction t WHERE t.status = :status")
    Long countByStatus(@Param("status") String status);
}
