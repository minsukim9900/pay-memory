package site.paymemory.support;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import site.paymemory.domain.transaction.entity.PaymentTransaction;
import site.paymemory.domain.transaction.repository.PaymentTransactionRepositoryPort;

public class FakePaymentTransactionRepositoryPort implements PaymentTransactionRepositoryPort {

    private final List<PaymentTransaction> paymentTransactions = new ArrayList<>();
    private long sequence = 1L;

    @Override
    public PaymentTransaction save(PaymentTransaction paymentTransaction) {

        setField(paymentTransaction, "id", sequence++);
        paymentTransactions.add(paymentTransaction);

        return paymentTransaction;
    }

    @Override
    public boolean existsByUserIdAndTransactionAtAndMerchantNameAndAmount(
            Long userId,
            Instant transactionAt,
            String merchantName,
            long amount
    ) {

        return paymentTransactions.stream()
                .anyMatch(paymentTransaction ->
                        paymentTransaction.getUser().getId().equals(userId)
                                && paymentTransaction.getTransactionAt().equals(transactionAt)
                                && paymentTransaction.getMerchantName().equals(merchantName)
                                && paymentTransaction.getAmount() == amount
                );
    }

    public List<PaymentTransaction> findAll() {

        return paymentTransactions;
    }

    private void setField(
            PaymentTransaction paymentTransaction,
            String fieldName,
            Object value
    ) {

        try {
            Field field = PaymentTransaction.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(paymentTransaction, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException("PaymentTransaction 테스트 필드 설정에 실패했습니다.", e);
        }
    }
}