package site.paymemory.domain.transaction.repository;

import java.time.Instant;
import java.util.List;

import site.paymemory.domain.transaction.entity.PaymentTransaction;

public interface PaymentTransactionRepositoryPort {

    PaymentTransaction save(PaymentTransaction paymentTransaction);

    <S extends PaymentTransaction> List<S> saveAll(Iterable<S> paymentTransactions);

    List<PaymentTransaction> findByUserIdAndTransactionAtBetween(
            Long userId,
            Instant startTransactionAt,
            Instant endTransactionAt
    );
}