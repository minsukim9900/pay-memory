package site.paymemory.domain.transaction.dto.response;

public record UploadTransactionExcelResponse(
        int totalCount,
        int savedCount,
        int duplicatedCount,
        int failedCount
) {

    public static UploadTransactionExcelResponse of(
            int totalCount,
            int savedCount,
            int duplicatedCount,
            int failedCount
    ) {

        return new UploadTransactionExcelResponse(
                totalCount,
                savedCount,
                duplicatedCount,
                failedCount
        );
    }
}