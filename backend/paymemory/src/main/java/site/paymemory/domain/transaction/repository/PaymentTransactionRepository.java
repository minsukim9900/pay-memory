package site.paymemory.domain.transaction.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import site.paymemory.domain.transaction.entity.PaymentTransaction;

public interface PaymentTransactionRepository extends
        JpaRepository<PaymentTransaction, Long>,
        PaymentTransactionRepositoryPort {

    @Override
    @Query("""
            SELECT paymentTransaction
            FROM PaymentTransaction paymentTransaction
            WHERE paymentTransaction.user.id = :userId
              AND paymentTransaction.transactionAt BETWEEN :startTransactionAt AND :endTransactionAt
            """)
    List<PaymentTransaction> findByUserIdAndTransactionAtBetween(
            @Param("userId") Long userId,
            @Param("startTransactionAt") Instant startTransactionAt,
            @Param("endTransactionAt") Instant endTransactionAt
    );
}