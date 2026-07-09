package site.paymemory.domain.transaction.service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.paymemory.domain.transaction.dto.excel.KakaoPayTransactionDuplicateKey;
import site.paymemory.domain.transaction.dto.excel.KakaoPayTransactionExcelRow;
import site.paymemory.domain.transaction.dto.request.UploadTransactionExcelRequest;
import site.paymemory.domain.transaction.dto.response.UploadTransactionExcelResponse;
import site.paymemory.domain.transaction.entity.PaymentTransaction;
import site.paymemory.domain.transaction.entity.TransactionCategory;
import site.paymemory.domain.transaction.entity.TransactionType;
import site.paymemory.domain.transaction.exception.TransactionErrorCode;
import site.paymemory.domain.transaction.excel.KakaoPayExcelParser;
import site.paymemory.domain.transaction.repository.PaymentTransactionRepositoryPort;
import site.paymemory.domain.transaction.repository.TransactionCategoryRepositoryPort;
import site.paymemory.domain.user.entity.User;
import site.paymemory.domain.user.exception.UserErrorCode;
import site.paymemory.domain.user.repository.UserRepositoryPort;
import site.paymemory.global.exception.GlobalException;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionExcelUploadService {

    private static final String UNCATEGORIZED_CATEGORY_NAME = "미분류";

    private final KakaoPayExcelParser kakaoPayExcelParser;
    private final UserRepositoryPort userRepositoryPort;
    private final PaymentTransactionRepositoryPort paymentTransactionRepositoryPort;
    private final TransactionCategoryRepositoryPort transactionCategoryRepositoryPort;

    public UploadTransactionExcelResponse upload(UploadTransactionExcelRequest request) {

        User user = findUser(request.userId());
        List<KakaoPayTransactionExcelRow> rows = kakaoPayExcelParser.parse(
                request.file(),
                request.filePassword()
        );
        TransactionCategory transactionCategory = findUncategorizedCategory();

        Set<KakaoPayTransactionDuplicateKey> existingKeys = findExistingKeys(
                user.getId(),
                rows
        );

        Set<KakaoPayTransactionDuplicateKey> processedKeys = new HashSet<>();
        List<PaymentTransaction> paymentTransactions = rows.stream()
                .filter(row -> isNotDuplicated(
                        row,
                        existingKeys,
                        processedKeys
                ))
                .map(row -> createPaymentTransaction(
                        user,
                        transactionCategory,
                        row
                ))
                .toList();

        paymentTransactionRepositoryPort.saveAll(paymentTransactions);

        int savedCount = paymentTransactions.size();
        int duplicatedCount = rows.size() - savedCount;

        return UploadTransactionExcelResponse.of(
                rows.size(),
                savedCount,
                duplicatedCount,
                0
        );
    }

    private User findUser(Long userId) {

        return userRepositoryPort.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new GlobalException(UserErrorCode.USER_NOT_FOUND));
    }

    private TransactionCategory findUncategorizedCategory() {

        return transactionCategoryRepositoryPort.findByName(UNCATEGORIZED_CATEGORY_NAME)
                .orElseThrow(() -> new GlobalException(TransactionErrorCode.TRANSACTION_CATEGORY_NOT_FOUND));
    }

    private Set<KakaoPayTransactionDuplicateKey> findExistingKeys(
            Long userId,
            List<KakaoPayTransactionExcelRow> rows
    ) {

        Instant startTransactionAt = findStartTransactionAt(rows);
        Instant endTransactionAt = findEndTransactionAt(rows);

        List<PaymentTransaction> paymentTransactions =
                paymentTransactionRepositoryPort.findByUserIdAndTransactionAtBetween(
                        userId,
                        startTransactionAt,
                        endTransactionAt
                );

        Set<KakaoPayTransactionDuplicateKey> existingKeys = new HashSet<>();

        for (PaymentTransaction paymentTransaction : paymentTransactions) {
            existingKeys.add(KakaoPayTransactionDuplicateKey.from(paymentTransaction));
        }

        return existingKeys;
    }

    private Instant findStartTransactionAt(List<KakaoPayTransactionExcelRow> rows) {

        return rows.stream()
                .map(KakaoPayTransactionExcelRow::transactionAt)
                .min(Instant::compareTo)
                .orElseThrow(() -> new GlobalException(TransactionErrorCode.TRANSACTION_EXCEL_EMPTY));
    }

    private Instant findEndTransactionAt(List<KakaoPayTransactionExcelRow> rows) {

        return rows.stream()
                .map(KakaoPayTransactionExcelRow::transactionAt)
                .max(Instant::compareTo)
                .orElseThrow(() -> new GlobalException(TransactionErrorCode.TRANSACTION_EXCEL_EMPTY));
    }

    private boolean isNotDuplicated(
            KakaoPayTransactionExcelRow row,
            Set<KakaoPayTransactionDuplicateKey> existingKeys,
            Set<KakaoPayTransactionDuplicateKey> processedKeys
    ) {

        KakaoPayTransactionDuplicateKey key = KakaoPayTransactionDuplicateKey.from(row);

        if (existingKeys.contains(key)) {
            return false;
        }

        if (processedKeys.contains(key)) {
            return false;
        }

        processedKeys.add(key);

        return true;
    }

    private PaymentTransaction createPaymentTransaction(
            User user,
            TransactionCategory transactionCategory,
            KakaoPayTransactionExcelRow row
    ) {

        return PaymentTransaction.of(
                user,
                transactionCategory,
                row.transactionAt(),
                row.merchantName(),
                row.amount(),
                TransactionType.UNCLASSIFIED,
                isIncludedInSpending(row.amount()),
                null
        );
    }

    private boolean isIncludedInSpending(long amount) {

        return amount < 0;
    }
}