package site.paymemory.domain.transaction.service;

import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import site.paymemory.domain.transaction.dto.request.UploadTransactionExcelRequest;
import site.paymemory.domain.transaction.dto.response.UploadTransactionExcelResponse;
import site.paymemory.domain.transaction.entity.PaymentTransaction;
import site.paymemory.domain.transaction.entity.TransactionCategory;
import site.paymemory.domain.transaction.entity.TransactionType;
import site.paymemory.domain.transaction.excel.KakaoPayExcelParser;
import site.paymemory.domain.user.entity.User;
import site.paymemory.domain.user.exception.UserErrorCode;
import site.paymemory.global.exception.GlobalException;
import site.paymemory.support.FakePaymentTransactionRepositoryPort;
import site.paymemory.support.FakeTransactionCategoryRepositoryPort;
import site.paymemory.support.FakeUserRepositoryPort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static site.paymemory.support.PaymentTransactionTestFactory.createPaymentTransaction;
import static site.paymemory.support.TransactionCategoryTestFactory.createTransactionCategory;
import static site.paymemory.support.UserTestFactory.createUser;

class TransactionExcelUploadServiceTest {

    private static final Long USER_ID = 1L;
    private static final String UNCATEGORIZED_CATEGORY_NAME = "미분류";

    private final KakaoPayExcelParser kakaoPayExcelParser = new KakaoPayExcelParser();
    private final FakeUserRepositoryPort userRepositoryPort = new FakeUserRepositoryPort();
    private final FakePaymentTransactionRepositoryPort paymentTransactionRepositoryPort =
            new FakePaymentTransactionRepositoryPort();
    private final FakeTransactionCategoryRepositoryPort transactionCategoryRepositoryPort =
            new FakeTransactionCategoryRepositoryPort();

    private final TransactionExcelUploadService transactionExcelUploadService =
            new TransactionExcelUploadService(
                    kakaoPayExcelParser,
                    userRepositoryPort,
                    paymentTransactionRepositoryPort,
                    transactionCategoryRepositoryPort
            );

    @Nested
    @DisplayName("거래내역 엑셀 업로드")
    class Upload {

        @Test
        @DisplayName("중복 거래내역을 제외하고 신규 거래내역만 저장한다.")
        void uploadTransactionExcelSuccessfully() throws Exception {
            //given
            User user = createUser();
            ReflectionTestUtils.setField(user, "id", USER_ID);
            userRepositoryPort.save(USER_ID, user);

            TransactionCategory transactionCategory = createTransactionCategory(UNCATEGORIZED_CATEGORY_NAME);
            transactionCategoryRepositoryPort.save(transactionCategory);

            PaymentTransaction duplicatedPaymentTransaction = createPaymentTransaction(
                    user,
                    transactionCategory,
                    Instant.parse("2026-06-15T17:09:41Z"),
                    "GS25관악원",
                    -3600L,
                    TransactionType.UNCLASSIFIED,
                    true,
                    null
            );

            paymentTransactionRepositoryPort.save(duplicatedPaymentTransaction);

            MockMultipartFile file = createExcelFile(
                    "transactions.xlsx",
                    new String[][]{
                            {"날짜", "사용처", "금액", "상태"},
                            {"2026-06-16 02:09:41", "ＧＳ２５관악원", "-3,600원", ""},
                            {"2026-06-15 15:03:27", "삼성SSAFY", "+1,000,000원", ""},
                            {"2026-06-14 12:10:00", "투썸플레이스 서울대", "-5,500원", ""}
                    }
            );

            UploadTransactionExcelRequest request = UploadTransactionExcelRequest.of(
                    USER_ID,
                    file,
                    null
            );

            //when
            UploadTransactionExcelResponse response = transactionExcelUploadService.upload(request);

            //then
            assertThat(response.totalCount()).isEqualTo(3);
            assertThat(response.savedCount()).isEqualTo(2);
            assertThat(response.duplicatedCount()).isEqualTo(1);
            assertThat(response.failedCount()).isEqualTo(0);

            List<PaymentTransaction> paymentTransactions = paymentTransactionRepositoryPort.findAll();

            assertThat(paymentTransactions).hasSize(3);

            PaymentTransaction savedIncomeTransaction = paymentTransactions.get(1);

            assertThat(savedIncomeTransaction.getUser()).isEqualTo(user);
            assertThat(savedIncomeTransaction.getTransactionCategory()).isEqualTo(transactionCategory);
            assertThat(savedIncomeTransaction.getTransactionAt()).isEqualTo(Instant.parse("2026-06-15T06:03:27Z"));
            assertThat(savedIncomeTransaction.getMerchantName()).isEqualTo("삼성SSAFY");
            assertThat(savedIncomeTransaction.getAmount()).isEqualTo(1000000L);
            assertThat(savedIncomeTransaction.getTransactionType()).isEqualTo(TransactionType.UNCLASSIFIED);
            assertThat(savedIncomeTransaction.isIncludedInSpending()).isFalse();
            assertThat(savedIncomeTransaction.getMemo()).isNull();

            PaymentTransaction savedSpendingTransaction = paymentTransactions.get(2);

            assertThat(savedSpendingTransaction.getUser()).isEqualTo(user);
            assertThat(savedSpendingTransaction.getTransactionCategory()).isEqualTo(transactionCategory);
            assertThat(savedSpendingTransaction.getTransactionAt()).isEqualTo(Instant.parse("2026-06-14T03:10:00Z"));
            assertThat(savedSpendingTransaction.getMerchantName()).isEqualTo("투썸플레이스서울대");
            assertThat(savedSpendingTransaction.getAmount()).isEqualTo(-5500L);
            assertThat(savedSpendingTransaction.getTransactionType()).isEqualTo(TransactionType.UNCLASSIFIED);
            assertThat(savedSpendingTransaction.isIncludedInSpending()).isTrue();
            assertThat(savedSpendingTransaction.getMemo()).isNull();
        }

        @Test
        @DisplayName("미분류 카테고리가 없으면 새로 생성한다.")
        void createUncategorizedCategoryWhenNotExists() throws Exception {
            //given
            User user = createUser();
            ReflectionTestUtils.setField(user, "id", USER_ID);
            userRepositoryPort.save(USER_ID, user);

            MockMultipartFile file = createExcelFile(
                    "transactions.xlsx",
                    new String[][]{
                            {"날짜", "사용처", "금액", "상태"},
                            {"2026-06-16 02:09:41", "ＧＳ２５관악원", "-3,600원", ""}
                    }
            );

            UploadTransactionExcelRequest request = UploadTransactionExcelRequest.of(
                    USER_ID,
                    file,
                    null
            );

            //when
            UploadTransactionExcelResponse response = transactionExcelUploadService.upload(request);

            //then
            assertThat(response.totalCount()).isEqualTo(1);
            assertThat(response.savedCount()).isEqualTo(1);
            assertThat(response.duplicatedCount()).isEqualTo(0);
            assertThat(response.failedCount()).isEqualTo(0);

            assertThat(transactionCategoryRepositoryPort.findByName(UNCATEGORIZED_CATEGORY_NAME))
                    .isPresent();

            assertThat(paymentTransactionRepositoryPort.findAll()).hasSize(1);

            PaymentTransaction savedPaymentTransaction = paymentTransactionRepositoryPort.findAll()
                    .get(0);

            assertThat(savedPaymentTransaction.getTransactionCategory().getName())
                    .isEqualTo(UNCATEGORIZED_CATEGORY_NAME);
            assertThat(savedPaymentTransaction.getTransactionType())
                    .isEqualTo(TransactionType.UNCLASSIFIED);
            assertThat(savedPaymentTransaction.isIncludedInSpending())
                    .isTrue();
        }

        @Test
        @DisplayName("사용자가 존재하지 않으면 예외가 발생한다.")
        void throwExceptionWhenUserNotFound() throws Exception {
            //given
            MockMultipartFile file = createExcelFile(
                    "transactions.xlsx",
                    new String[][]{
                            {"날짜", "사용처", "금액", "상태"},
                            {"2026-06-16 02:09:41", "ＧＳ２５관악원", "-3,600원", ""}
                    }
            );

            UploadTransactionExcelRequest request = UploadTransactionExcelRequest.of(
                    USER_ID,
                    file,
                    null
            );

            //when
            Throwable thrown = catchThrowable(() ->
                    transactionExcelUploadService.upload(request)
            );

            //then
            assertThat(thrown)
                    .isInstanceOf(GlobalException.class);

            GlobalException exception = (GlobalException) thrown;

            assertThat(exception.getErrorCode())
                    .isEqualTo(UserErrorCode.USER_NOT_FOUND);
        }
    }

    private MockMultipartFile createExcelFile(
            String filename,
            String[][] values
    ) throws Exception {

        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
        ) {
            Sheet sheet = workbook.createSheet();

            for (int rowIndex = 0; rowIndex < values.length; rowIndex++) {
                Row row = sheet.createRow(rowIndex);

                for (int columnIndex = 0; columnIndex < values[rowIndex].length; columnIndex++) {
                    row.createCell(columnIndex)
                            .setCellValue(values[rowIndex][columnIndex]);
                }
            }

            workbook.write(outputStream);

            return new MockMultipartFile(
                    "file",
                    filename,
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    outputStream.toByteArray()
            );
        }
    }
}