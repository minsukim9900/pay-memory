package site.paymemory.domain.transaction.excel;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.poifs.crypt.Decryptor;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import site.paymemory.domain.transaction.dto.excel.KakaoPayTransactionExcelRow;
import site.paymemory.domain.transaction.exception.TransactionErrorCode;
import site.paymemory.domain.transaction.normalizer.KakaoPayMerchantNameNormalizer;
import site.paymemory.global.exception.GlobalException;

@Component
public class KakaoPayExcelParser {

    private static final String EXCEL_EXTENSION = ".xlsx";
    private static final int HEADER_ROW_INDEX = 0;
    private static final int DATA_START_ROW_INDEX = 1;

    private static final String TRANSACTION_AT_HEADER_NAME = "날짜";
    private static final String MERCHANT_NAME_HEADER_NAME = "사용처";
    private static final String AMOUNT_HEADER_NAME = "금액";
    private static final String STATUS_HEADER_NAME = "상태";

    private static final ZoneId KOREA_ZONE_ID = ZoneId.of("Asia/Seoul");
    private static final List<DateTimeFormatter> DATE_TIME_FORMATTERS = List.of(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    );

    private final DataFormatter dataFormatter = new DataFormatter();

    public List<KakaoPayTransactionExcelRow> parse(
            MultipartFile file,
            String filePassword
    ) {

        validateFile(file);

        try (InputStream inputStream = FileMagic.prepareToCheckMagic(file.getInputStream())) {
            FileMagic fileMagic = FileMagic.valueOf(inputStream);

            if (fileMagic == FileMagic.OLE2) {
                return parseEncryptedWorkbook(inputStream, filePassword);
            }

            if (fileMagic == FileMagic.OOXML) {
                return parsePlainWorkbook(inputStream);
            }

            throw new GlobalException(TransactionErrorCode.TRANSACTION_EXCEL_PARSE_FAILED);
        } catch (IOException e) {
            throw new GlobalException(TransactionErrorCode.TRANSACTION_EXCEL_PARSE_FAILED);
        }
    }

    private List<KakaoPayTransactionExcelRow> parsePlainWorkbook(InputStream inputStream) {

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            return parseWorkbook(workbook);
        } catch (IOException e) {
            throw new GlobalException(TransactionErrorCode.TRANSACTION_EXCEL_PARSE_FAILED);
        }
    }

    private List<KakaoPayTransactionExcelRow> parseEncryptedWorkbook(
            InputStream inputStream,
            String filePassword
    ) {

        if (filePassword == null || filePassword.isBlank()) {
            throw new GlobalException(TransactionErrorCode.TRANSACTION_EXCEL_PASSWORD_REQUIRED);
        }

        try (POIFSFileSystem fileSystem = new POIFSFileSystem(inputStream)) {
            EncryptionInfo encryptionInfo = new EncryptionInfo(fileSystem);
            Decryptor decryptor = Decryptor.getInstance(encryptionInfo);

            if (!decryptor.verifyPassword(filePassword)) {
                throw new GlobalException(TransactionErrorCode.TRANSACTION_EXCEL_INVALID_PASSWORD);
            }

            try (
                    InputStream dataStream = decryptor.getDataStream(fileSystem);
                    Workbook workbook = new XSSFWorkbook(dataStream)
            ) {
                return parseWorkbook(workbook);
            }
        } catch (GlobalException e) {
            throw e;
        } catch (GeneralSecurityException e) {
            throw new GlobalException(TransactionErrorCode.TRANSACTION_EXCEL_INVALID_PASSWORD);
        } catch (IOException e) {
            throw new GlobalException(TransactionErrorCode.TRANSACTION_EXCEL_PARSE_FAILED);
        }
    }

    private List<KakaoPayTransactionExcelRow> parseWorkbook(Workbook workbook) {

        Sheet sheet = workbook.getSheetAt(0);
        validateSheet(sheet);

        KakaoPayExcelHeader header = analyzeHeader(sheet.getRow(HEADER_ROW_INDEX));

        return parseRows(sheet, header);
    }

    private void validateFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new GlobalException(TransactionErrorCode.TRANSACTION_EXCEL_FILE_REQUIRED);
        }

        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null || !originalFilename.endsWith(EXCEL_EXTENSION)) {
            throw new GlobalException(TransactionErrorCode.TRANSACTION_EXCEL_INVALID_FILE_EXTENSION);
        }
    }

    private void validateSheet(Sheet sheet) {

        if (sheet == null || sheet.getPhysicalNumberOfRows() <= DATA_START_ROW_INDEX) {
            throw new GlobalException(TransactionErrorCode.TRANSACTION_EXCEL_EMPTY);
        }
    }

    private KakaoPayExcelHeader analyzeHeader(Row headerRow) {

        if (headerRow == null) {
            throw new GlobalException(TransactionErrorCode.TRANSACTION_EXCEL_REQUIRED_COLUMN_MISSING);
        }

        int transactionAtColumnIndex = KakaoPayExcelHeader.notFoundColumnIndex();
        int merchantNameColumnIndex = KakaoPayExcelHeader.notFoundColumnIndex();
        int amountColumnIndex = KakaoPayExcelHeader.notFoundColumnIndex();
        int statusColumnIndex = KakaoPayExcelHeader.notFoundColumnIndex();

        for (int columnIndex = 0; columnIndex < headerRow.getLastCellNum(); columnIndex++) {
            String headerName = getCellValue(headerRow, columnIndex);

            if (TRANSACTION_AT_HEADER_NAME.equals(headerName)) {
                transactionAtColumnIndex = columnIndex;
            }

            if (MERCHANT_NAME_HEADER_NAME.equals(headerName)) {
                merchantNameColumnIndex = columnIndex;
            }

            if (AMOUNT_HEADER_NAME.equals(headerName)) {
                amountColumnIndex = columnIndex;
            }

            if (STATUS_HEADER_NAME.equals(headerName)) {
                statusColumnIndex = columnIndex;
            }
        }

        validateRequiredColumn(
                transactionAtColumnIndex,
                merchantNameColumnIndex,
                amountColumnIndex
        );

        return KakaoPayExcelHeader.of(
                transactionAtColumnIndex,
                merchantNameColumnIndex,
                amountColumnIndex,
                statusColumnIndex
        );
    }

    private void validateRequiredColumn(
            int transactionAtColumnIndex,
            int merchantNameColumnIndex,
            int amountColumnIndex
    ) {

        if (
                transactionAtColumnIndex == KakaoPayExcelHeader.notFoundColumnIndex()
                        || merchantNameColumnIndex == KakaoPayExcelHeader.notFoundColumnIndex()
                        || amountColumnIndex == KakaoPayExcelHeader.notFoundColumnIndex()
        ) {
            throw new GlobalException(TransactionErrorCode.TRANSACTION_EXCEL_REQUIRED_COLUMN_MISSING);
        }
    }

    private List<KakaoPayTransactionExcelRow> parseRows(
            Sheet sheet,
            KakaoPayExcelHeader header
    ) {

        List<KakaoPayTransactionExcelRow> rows = new ArrayList<>();

        for (int rowIndex = DATA_START_ROW_INDEX; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);

            if (isEmptyRow(row)) {
                continue;
            }

            rows.add(parseRow(row, header));
        }

        if (rows.isEmpty()) {
            throw new GlobalException(TransactionErrorCode.TRANSACTION_EXCEL_EMPTY);
        }

        return rows;
    }

    private KakaoPayTransactionExcelRow parseRow(
            Row row,
            KakaoPayExcelHeader header
    ) {

        try {
            Instant transactionAt = parseTransactionAt(
                    getCellValue(row, header.transactionAtColumnIndex())
            );

            String merchantName = KakaoPayMerchantNameNormalizer.normalize(
                    getCellValue(row, header.merchantNameColumnIndex())
            );

            long amount = parseAmount(
                    getCellValue(row, header.amountColumnIndex())
            );

            return KakaoPayTransactionExcelRow.of(
                    transactionAt,
                    merchantName,
                    amount
            );
        } catch (RuntimeException e) {
            throw new GlobalException(TransactionErrorCode.TRANSACTION_EXCEL_INVALID_ROW);
        }
    }

    private Instant parseTransactionAt(String value) {

        for (DateTimeFormatter dateTimeFormatter : DATE_TIME_FORMATTERS) {
            try {
                LocalDateTime localDateTime = LocalDateTime.parse(
                        value,
                        dateTimeFormatter
                );

                return localDateTime
                        .atZone(KOREA_ZONE_ID)
                        .toInstant();
            } catch (RuntimeException ignored) {
            }
        }

        throw new GlobalException(TransactionErrorCode.TRANSACTION_EXCEL_INVALID_ROW);
    }

    private long parseAmount(String value) {

        String normalizedValue = value
                .replace(",", "")
                .replace("원", "")
                .replace("₩", "")
                .replace("+", "")
                .replace("−", "-")
                .replaceAll("\\s+", "")
                .trim();

        return Long.parseLong(normalizedValue);
    }

    private boolean isEmptyRow(Row row) {

        if (row == null) {
            return true;
        }

        for (int columnIndex = 0; columnIndex < row.getLastCellNum(); columnIndex++) {
            if (!getCellValue(row, columnIndex).isBlank()) {
                return false;
            }
        }

        return true;
    }

    private String getCellValue(
            Row row,
            int columnIndex
    ) {

        return dataFormatter.formatCellValue(row.getCell(columnIndex))
                .trim();
    }
}