package site.paymemory.domain.transaction.excel;

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

import site.paymemory.domain.transaction.dto.excel.KakaoPayTransactionExcelRow;
import site.paymemory.domain.transaction.exception.TransactionErrorCode;
import site.paymemory.global.exception.GlobalException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class KakaoPayExcelParserTest {

    private final KakaoPayExcelParser kakaoPayExcelParser = new KakaoPayExcelParser();

    @Nested
    @DisplayName("카카오페이 엑셀 파싱")
    class Parse {

        @Test
        @DisplayName("정상 엑셀 파일이면 거래내역 row 목록을 반환한다")
        void givenValidExcelFile_whenParse_thenReturnsTransactionRows() throws Exception {
            // given
            MockMultipartFile file = createExcelFile(
                    "transactions.xlsx",
                    new String[][]{
                            {"날짜", "사용처", "금액", "상태"},
                            {"2026-06-16 02:09:41", "ＧＳ２５관악원", "-3,600원", ""},
                            {"2026-06-15 15:03:27", "삼성SSAFY", "+1,000,000원", ""}
                    }
            );

            // when
            List<KakaoPayTransactionExcelRow> result = kakaoPayExcelParser.parse(file);

            // then
            assertThat(result).hasSize(2);

            assertThat(result.get(0).transactionAt())
                    .isEqualTo(Instant.parse("2026-06-15T17:09:41Z"));
            assertThat(result.get(0).merchantName())
                    .isEqualTo("GS25관악원");
            assertThat(result.get(0).amount())
                    .isEqualTo(-3600L);

            assertThat(result.get(1).transactionAt())
                    .isEqualTo(Instant.parse("2026-06-15T06:03:27Z"));
            assertThat(result.get(1).merchantName())
                    .isEqualTo("삼성SSAFY");
            assertThat(result.get(1).amount())
                    .isEqualTo(1000000L);
        }

        @Test
        @DisplayName("파일이 비어 있으면 예외가 발생한다")
        void givenEmptyFile_whenParse_thenThrowsException() {
            // given
            MockMultipartFile file = new MockMultipartFile(
                    "file",
                    "transactions.xlsx",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    new byte[0]
            );

            // when
            Throwable thrown = catchThrowable(() -> kakaoPayExcelParser.parse(file));

            // then
            assertThat(thrown)
                    .isInstanceOf(GlobalException.class);

            GlobalException exception = (GlobalException) thrown;

            assertThat(exception.getErrorCode())
                    .isEqualTo(TransactionErrorCode.TRANSACTION_EXCEL_FILE_REQUIRED);
        }

        @Test
        @DisplayName("xlsx 파일이 아니면 예외가 발생한다")
        void givenInvalidFileExtension_whenParse_thenThrowsException() throws Exception {
            // given
            MockMultipartFile file = createExcelFile(
                    "transactions.csv",
                    new String[][]{
                            {"날짜", "사용처", "금액", "상태"},
                            {"2026-06-16 02:09:41", "ＧＳ２５관악원", "-3,600원", ""}
                    }
            );

            // when
            Throwable thrown = catchThrowable(() -> kakaoPayExcelParser.parse(file));

            // then
            assertThat(thrown)
                    .isInstanceOf(GlobalException.class);

            GlobalException exception = (GlobalException) thrown;

            assertThat(exception.getErrorCode())
                    .isEqualTo(TransactionErrorCode.TRANSACTION_EXCEL_INVALID_FILE_EXTENSION);
        }

        @Test
        @DisplayName("필수 컬럼이 없으면 예외가 발생한다")
        void givenMissingRequiredColumn_whenParse_thenThrowsException() throws Exception {
            // given
            MockMultipartFile file = createExcelFile(
                    "transactions.xlsx",
                    new String[][]{
                            {"날짜", "사용처", "상태"},
                            {"2026-06-16 02:09:41", "ＧＳ２５관악원", ""}
                    }
            );

            // when
            Throwable thrown = catchThrowable(() -> kakaoPayExcelParser.parse(file));

            // then
            assertThat(thrown)
                    .isInstanceOf(GlobalException.class);

            GlobalException exception = (GlobalException) thrown;

            assertThat(exception.getErrorCode())
                    .isEqualTo(TransactionErrorCode.TRANSACTION_EXCEL_REQUIRED_COLUMN_MISSING);
        }

        @Test
        @DisplayName("올바르지 않은 거래내역 행이면 예외가 발생한다")
        void givenInvalidRow_whenParse_thenThrowsException() throws Exception {
            // given
            MockMultipartFile file = createExcelFile(
                    "transactions.xlsx",
                    new String[][]{
                            {"날짜", "사용처", "금액", "상태"},
                            {"올바르지 않은 날짜", "ＧＳ２５관악원", "-3,600원", ""}
                    }
            );

            // when
            Throwable thrown = catchThrowable(() -> kakaoPayExcelParser.parse(file));

            // then
            assertThat(thrown)
                    .isInstanceOf(GlobalException.class);

            GlobalException exception = (GlobalException) thrown;

            assertThat(exception.getErrorCode())
                    .isEqualTo(TransactionErrorCode.TRANSACTION_EXCEL_INVALID_ROW);
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