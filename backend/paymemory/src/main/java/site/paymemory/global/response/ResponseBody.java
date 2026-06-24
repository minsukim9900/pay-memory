package site.paymemory.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import site.paymemory.global.exception.ErrorCode;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.*;
import static lombok.AccessLevel.*;

@Getter
@AllArgsConstructor(access = PRIVATE)
@Builder(access = PRIVATE)
public class ResponseBody<T> {

    private final int statusCode;
    private final String message;

    @JsonInclude(NON_NULL)
    private final Integer errorCode;

    @JsonInclude(NON_NULL)
    private final T data;

    public static <T> ResponseBody<T> success(T data) {
        return ResponseBody.<T>builder()
                .statusCode(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(data)
                .build();
    }

    public static ResponseBody<Void> success() {
        return ResponseBody.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .build();
    }

    public static ResponseBody<Void> error(ErrorCode errorCode) {
        return ResponseBody.<Void>builder()
                .statusCode(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .errorCode(errorCode.getCode())
                .build();
    }
}
