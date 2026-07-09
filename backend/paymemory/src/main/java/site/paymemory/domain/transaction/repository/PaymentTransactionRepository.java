package site.paymemory.domain.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import site.paymemory.domain.transaction.entity.PaymentTransaction;

public interface PaymentTransactionRepository extends
        JpaRepository<PaymentTransaction, Long>,
        PaymentTransactionRepositoryPort,
        PaymentTransactionCustomRepository {
}