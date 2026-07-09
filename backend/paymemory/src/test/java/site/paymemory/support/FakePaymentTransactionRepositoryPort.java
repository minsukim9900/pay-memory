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
    public <S extends PaymentTransaction> List<S> saveAll(Iterable<S> paymentTransactions) {

        List<S> savedPaymentTransactions = new ArrayList<>();

        for (S paymentTransaction : paymentTransactions) {
            setField(paymentTransaction, "id", sequence++);
            this.paymentTransactions.add(paymentTransaction);
            savedPaymentTransactions.add(paymentTransaction);
        }

        return savedPaymentTransactions;
    }

    @Override
    public List<PaymentTransaction> findByUserIdAndTransactionAtBetween(
            Long userId,
            Instant startTransactionAt,
            Instant endTransactionAt
    ) {

        return paymentTransactions.stream()
                .filter(paymentTransaction -> paymentTransaction.getUser().getId().equals(userId))
                .filter(paymentTransaction -> !paymentTransaction.getTransactionAt().isBefore(startTransactionAt))
                .filter(paymentTransaction -> !paymentTransaction.getTransactionAt().isAfter(endTransactionAt))
                .toList();
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