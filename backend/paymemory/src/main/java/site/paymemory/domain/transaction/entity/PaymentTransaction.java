package site.paymemory.domain.transaction.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.paymemory.domain.user.entity.User;
import site.paymemory.global.entity.BaseTimeEntity;

import java.time.Instant;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Getter
@Entity
@Table(name = "payment_transaction")
@NoArgsConstructor(access = PROTECTED)
public class PaymentTransaction extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "payment_transaction_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "transaction_category_id", nullable = false)
    private TransactionCategory transactionCategory;

    @Column(name = "transaction_at", nullable = false)
    private Instant transactionAt;

    @Column(name = "merchant_name", nullable = false, length = 100)
    private String merchantName;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Enumerated(STRING)
    @Column(name = "transaction_type", nullable = false, length = 20)
    private TransactionType transactionType;

    @Column(name = "included_in_spending", nullable = false)
    private Boolean includedInSpending;

    @Column(name = "memo", length = 500)
    private String memo;

    @Column(name = "ai_image_object_key", length = 500)
    private String aiImageObjectKey;

    @Column(name = "ai_image_generated_at")
    private Instant aiImageGeneratedAt;

    @Builder(access = PRIVATE)
    private PaymentTransaction(
            User user,
            TransactionCategory transactionCategory,
            Instant transactionAt,
            String merchantName,
            Long amount,
            TransactionType transactionType,
            Boolean includedInSpending,
            String memo
    ) {
        this.user = user;
        this.transactionCategory = transactionCategory;
        this.transactionAt = transactionAt;
        this.merchantName = merchantName;
        this.amount = amount;
        this.transactionType = transactionType;
        this.includedInSpending = includedInSpending;
        this.memo = memo;
    }

    public static PaymentTransaction of(
            User user,
            TransactionCategory transactionCategory,
            Instant transactionAt,
            String merchantName,
            Long amount,
            TransactionType transactionType,
            Boolean includedInSpending,
            String memo
    ) {

        return PaymentTransaction
                .builder()
                .user(user)
                .transactionCategory(transactionCategory)
                .transactionAt(transactionAt)
                .merchantName(merchantName)
                .amount(amount)
                .transactionType(transactionType)
                .includedInSpending(includedInSpending)
                .memo(memo)
                .build();
    }

    public void updateTransaction(
            TransactionCategory transactionCategory,
            Instant transactionAt,
            String merchantName,
            Long amount,
            TransactionType transactionType,
            Boolean includedInSpending,
            String memo
    ) {
        this.transactionCategory = transactionCategory;
        this.transactionAt = transactionAt;
        this.merchantName = merchantName;
        this.amount = amount;
        this.transactionType = transactionType;
        this.includedInSpending = includedInSpending;
        this.memo = memo;
    }

    public void updateAiImage(String aiImageObjectKey, Instant aiImageGeneratedAt) {
        this.aiImageObjectKey = aiImageObjectKey;
        this.aiImageGeneratedAt = aiImageGeneratedAt;
    }
}