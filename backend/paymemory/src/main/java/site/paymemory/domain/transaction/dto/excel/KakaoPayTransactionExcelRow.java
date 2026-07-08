package site.paymemory.domain.transaction.dto.excel;

import java.time.Instant;

public record KakaoPayTransactionExcelRow(
        Instant transactionAt,
        String merchantName,
        long amount
) {

    public static KakaoPayTransactionExcelRow of(
            Instant transactionAt,
            String merchantName,
            long amount
    ) {

        return new KakaoPayTransactionExcelRow(
                transactionAt,
                merchantName,
                amount
        );
    }
}