package site.paymemory.domain.transaction.repository;

import static site.paymemory.domain.transaction.entity.QPaymentTransaction.paymentTransaction;

import java.time.Instant;
import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import site.paymemory.domain.transaction.entity.PaymentTransaction;

@RequiredArgsConstructor
public class PaymentTransactionCustomRepositoryImpl implements PaymentTransactionCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PaymentTransaction> findByUserIdAndTransactionAtBetween(
            Long userId,
            Instant startTransactionAt,
            Instant endTransactionAt
    ) {

        return queryFactory
                .selectFrom(paymentTransaction)
                .where(
                        paymentTransaction.user.id.eq(userId),
                        paymentTransaction.transactionAt.between(
                                startTransactionAt,
                                endTransactionAt
                        )
                )
                .fetch();
    }
}