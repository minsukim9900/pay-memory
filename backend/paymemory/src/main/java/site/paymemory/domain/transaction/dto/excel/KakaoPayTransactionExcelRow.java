package site.paymemory.domain.transaction.dto.excel;

import java.time.Instant;

import site.paymemory.domain.transaction.entity.TransactionType;

public record KakaoPayTransactionExcelRow(
        Instant transactionAt,
        String merchantName,
        long amount,
        TransactionType transactionType
) {

    public static KakaoPayTransactionExcelRow of(
            Instant transactionAt,
            String merchantName,
            long amount,
            TransactionType transactionType
    ) {

        return new KakaoPayTransactionExcelRow(
                transactionAt,
                merchantName,
                amount,
                transactionType
        );
    }
}