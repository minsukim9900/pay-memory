package site.paymemory.domain.transaction.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.paymemory.domain.transaction.dto.excel.KakaoPayTransactionExcelRow;
import site.paymemory.domain.transaction.dto.request.UploadTransactionExcelRequest;
import site.paymemory.domain.transaction.dto.response.UploadTransactionExcelResponse;
import site.paymemory.domain.transaction.entity.PaymentTransaction;
import site.paymemory.domain.transaction.entity.TransactionCategory;
import site.paymemory.domain.transaction.entity.TransactionType;
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
        List<KakaoPayTransactionExcelRow> rows = kakaoPayExcelParser.parse(request.file());
        TransactionCategory transactionCategory = findOrCreateUncategorizedCategory();

        int savedCount = 0;
        int duplicatedCount = 0;

        for (KakaoPayTransactionExcelRow row : rows) {
            if (isDuplicated(user.getId(), row)) {
                duplicatedCount++;
                continue;
            }

            PaymentTransaction paymentTransaction = createPaymentTransaction(
                    user,
                    transactionCategory,
                    row
            );

            paymentTransactionRepositoryPort.save(paymentTransaction);
            savedCount++;
        }

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

    private TransactionCategory findOrCreateUncategorizedCategory() {

        return transactionCategoryRepositoryPort.findByName(UNCATEGORIZED_CATEGORY_NAME)
                .orElseGet(() -> transactionCategoryRepositoryPort.save(
                        TransactionCategory.fromName(UNCATEGORIZED_CATEGORY_NAME)
                ));
    }

    private boolean isDuplicated(
            Long userId,
            KakaoPayTransactionExcelRow row
    ) {

        return paymentTransactionRepositoryPort.existsDuplicate(
                userId,
                row.transactionAt(),
                row.merchantName(),
                row.amount()
        );
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