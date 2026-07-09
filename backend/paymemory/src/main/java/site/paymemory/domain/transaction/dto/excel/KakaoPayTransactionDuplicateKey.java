package site.paymemory.domain.transaction.dto.excel;

import java.time.Instant;

import site.paymemory.domain.transaction.entity.PaymentTransaction;

public record KakaoPayTransactionDuplicateKey(
        Instant transactionAt,
        String merchantName,
        long amount
) {

    public static KakaoPayTransactionDuplicateKey from(KakaoPayTransactionExcelRow row) {

        return new KakaoPayTransactionDuplicateKey(
                row.transactionAt(),
                row.merchantName(),
                row.amount()
        );
    }

    public static KakaoPayTransactionDuplicateKey from(PaymentTransaction paymentTransaction) {

        return new KakaoPayTransactionDuplicateKey(
                paymentTransaction.getTransactionAt(),
                paymentTransaction.getMerchantName(),
                paymentTransaction.getAmount()
        );
    }
}