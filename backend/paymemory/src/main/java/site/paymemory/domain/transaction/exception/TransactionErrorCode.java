package site.paymemory.domain.transaction.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import site.paymemory.global.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum TransactionErrorCode implements ErrorCode {

    TRANSACTION_EXCEL_FILE_REQUIRED(
            HttpStatus.BAD_REQUEST,
            3000,
            "업로드할 엑셀 파일이 필요합니다."
    ),

    TRANSACTION_EXCEL_INVALID_FILE_EXTENSION(
            HttpStatus.BAD_REQUEST,
            3001,
            "지원하지 않는 엑셀 파일 형식입니다. .xlsx 파일만 업로드할 수 있습니다."
    ),

    TRANSACTION_EXCEL_PARSE_FAILED(
            HttpStatus.BAD_REQUEST,
            3002,
            "엑셀 파일을 분석할 수 없습니다."
    ),

    TRANSACTION_EXCEL_PASSWORD_REQUIRED(
            HttpStatus.BAD_REQUEST,
            3003,
            "비밀번호가 필요한 엑셀 파일입니다."
    ),

    TRANSACTION_EXCEL_INVALID_PASSWORD(
            HttpStatus.BAD_REQUEST,
            3004,
            "엑셀 파일 비밀번호가 올바르지 않습니다."
    ),

    TRANSACTION_EXCEL_EMPTY(
            HttpStatus.BAD_REQUEST,
            3005,
            "엑셀 파일에 거래내역이 존재하지 않습니다."
    ),

    TRANSACTION_EXCEL_REQUIRED_COLUMN_MISSING(
            HttpStatus.BAD_REQUEST,
            3006,
            "엑셀 파일에 필수 컬럼이 존재하지 않습니다."
    ),

    TRANSACTION_EXCEL_INVALID_ROW(
            HttpStatus.BAD_REQUEST,
            3007,
            "엑셀 파일에 올바르지 않은 거래내역 행이 존재합니다."
    ),

    TRANSACTION_CATEGORY_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            3008,
            "거래내역 카테고리를 찾을 수 없습니다."
    );

    private final HttpStatus status;
    private final int code;
    private final String message;
}