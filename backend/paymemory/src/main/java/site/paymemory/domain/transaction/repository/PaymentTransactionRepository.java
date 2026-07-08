package site.paymemory.domain.transaction.repository;

import java.time.Instant;

import org.springframework.data.jpa.repository.JpaRepository;

import site.paymemory.domain.transaction.entity.PaymentTransaction;

public interface PaymentTransactionRepository extends
        JpaRepository<PaymentTransaction, Long>,
        PaymentTransactionRepositoryPort {

    @Override
    boolean existsByUserIdAndTransactionAtAndMerchantNameAndAmount(
            Long userId,
            Instant transactionAt,
            String merchantName,
            long amount
    );
}