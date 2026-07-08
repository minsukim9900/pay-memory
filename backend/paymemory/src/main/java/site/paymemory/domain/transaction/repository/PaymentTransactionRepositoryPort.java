package site.paymemory.domain.transaction.repository;

import java.time.Instant;

import site.paymemory.domain.transaction.entity.PaymentTransaction;

public interface PaymentTransactionRepositoryPort {

    PaymentTransaction save(PaymentTransaction paymentTransaction);

    boolean existsByUserIdAndTransactionAtAndMerchantNameAndAmount(
            Long userId,
            Instant transactionAt,
            String merchantName,
            long amount
    );
}