package site.paymemory.support;

import site.paymemory.domain.transaction.entity.PaymentTransaction;
import site.paymemory.domain.transaction.entity.TransactionCategory;
import site.paymemory.domain.transaction.entity.TransactionType;
import site.paymemory.domain.user.entity.User;

import java.time.Instant;

import static site.paymemory.support.TransactionCategoryTestFactory.createTransactionCategory;
import static site.paymemory.support.UserTestFactory.createUser;

public final class PaymentTransactionTestFactory {

    public static final String TRANSACTION_CATEGORY_NAME = "식비";

    public static final Instant TRANSACTION_AT = Instant.parse("2026-06-29T10:00:00Z");
    public static final String MERCHANT_NAME = "스타벅스";
    public static final long AMOUNT = 5_000L;
    public static final TransactionType TRANSACTION_TYPE = TransactionType.PAYMENT;
    public static final boolean INCLUDED_IN_SPENDING = true;
    public static final String MEMO = "아이스 아메리카노";

    public static final Instant NEW_TRANSACTION_AT = Instant.parse("2026-06-30T10:00:00Z");
    public static final String NEW_TRANSACTION_CATEGORY_NAME = "이체";
    public static final String NEW_MERCHANT_NAME = "카카오페이";
    public static final long NEW_AMOUNT = 10_000L;
    public static final TransactionType NEW_TRANSACTION_TYPE = TransactionType.TRANSFER;
    public static final boolean NEW_INCLUDED_IN_SPENDING = false;
    public static final String NEW_MEMO = "친구 송금";

    public static final String AI_IMAGE_OBJECT_KEY = "ai-images/payment-transaction-1.png";
    public static final Instant AI_IMAGE_GENERATED_AT = Instant.parse("2026-06-29T11:00:00Z");

    private PaymentTransactionTestFactory() {
    }

    public static PaymentTransaction createPaymentTransaction() {

        return createPaymentTransaction(
                createUser(),
                createTransactionCategory(TRANSACTION_CATEGORY_NAME),
                TRANSACTION_AT,
                MERCHANT_NAME,
                AMOUNT,
                TRANSACTION_TYPE,
                INCLUDED_IN_SPENDING,
                MEMO
        );
    }

    public static PaymentTransaction createPaymentTransactionWithUser(User user) {

        return createPaymentTransaction(
                user,
                createTransactionCategory(TRANSACTION_CATEGORY_NAME),
                TRANSACTION_AT,
                MERCHANT_NAME,
                AMOUNT,
                TRANSACTION_TYPE,
                INCLUDED_IN_SPENDING,
                MEMO
        );
    }

    public static PaymentTransaction createPaymentTransactionWithCategory(TransactionCategory transactionCategory) {

        return createPaymentTransaction(
                createUser(),
                transactionCategory,
                TRANSACTION_AT,
                MERCHANT_NAME,
                AMOUNT,
                TRANSACTION_TYPE,
                INCLUDED_IN_SPENDING,
                MEMO
        );
    }

    public static PaymentTransaction createPaymentTransactionWithTransactionAt(Instant transactionAt) {

        return createPaymentTransaction(
                createUser(),
                createTransactionCategory(TRANSACTION_CATEGORY_NAME),
                transactionAt,
                MERCHANT_NAME,
                AMOUNT,
                TRANSACTION_TYPE,
                INCLUDED_IN_SPENDING,
                MEMO
        );
    }

    public static PaymentTransaction createPaymentTransactionWithMerchantName(String merchantName) {

        return createPaymentTransaction(
                createUser(),
                createTransactionCategory(TRANSACTION_CATEGORY_NAME),
                TRANSACTION_AT,
                merchantName,
                AMOUNT,
                TRANSACTION_TYPE,
                INCLUDED_IN_SPENDING,
                MEMO
        );
    }

    public static PaymentTransaction createPaymentTransactionWithAmount(long amount) {

        return createPaymentTransaction(
                createUser(),
                createTransactionCategory(TRANSACTION_CATEGORY_NAME),
                TRANSACTION_AT,
                MERCHANT_NAME,
                amount,
                TRANSACTION_TYPE,
                INCLUDED_IN_SPENDING,
                MEMO
        );
    }

    public static PaymentTransaction createPaymentTransactionWithTransactionType(TransactionType transactionType) {

        return createPaymentTransaction(
                createUser(),
                createTransactionCategory(TRANSACTION_CATEGORY_NAME),
                TRANSACTION_AT,
                MERCHANT_NAME,
                AMOUNT,
                transactionType,
                INCLUDED_IN_SPENDING,
                MEMO
        );
    }

    public static PaymentTransaction createPaymentTransaction(
            User user,
            TransactionCategory transactionCategory,
            Instant transactionAt,
            String merchantName,
            long amount,
            TransactionType transactionType,
            boolean includedInSpending,
            String memo
    ) {

        return PaymentTransaction.of(
                user,
                transactionCategory,
                transactionAt,
                merchantName,
                amount,
                transactionType,
                includedInSpending,
                memo
        );
    }
}