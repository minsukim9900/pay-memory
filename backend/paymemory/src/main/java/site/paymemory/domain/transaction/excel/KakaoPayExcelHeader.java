package site.paymemory.domain.transaction.excel;

public record KakaoPayExcelHeader(
        int transactionAtColumnIndex,
        int merchantNameColumnIndex,
        int amountColumnIndex,
        int statusColumnIndex
) {

    private static final int NOT_FOUND_COLUMN_INDEX = -1;

    public static KakaoPayExcelHeader of(
            int transactionAtColumnIndex,
            int merchantNameColumnIndex,
            int amountColumnIndex,
            int statusColumnIndex
    ) {

        return new KakaoPayExcelHeader(
                transactionAtColumnIndex,
                merchantNameColumnIndex,
                amountColumnIndex,
                statusColumnIndex
        );
    }

    public boolean hasStatusColumn() {

        return statusColumnIndex != NOT_FOUND_COLUMN_INDEX;
    }

    public static int notFoundColumnIndex() {

        return NOT_FOUND_COLUMN_INDEX;
    }
}