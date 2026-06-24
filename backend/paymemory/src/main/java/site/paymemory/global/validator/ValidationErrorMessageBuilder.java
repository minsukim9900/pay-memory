package site.paymemory.global.validator;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.stream.Collectors;

public final class ValidationErrorMessageBuilder {

    private ValidationErrorMessageBuilder() {
    }

    public static String build(MethodArgumentNotValidException e) {
        return e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(ValidationErrorMessageBuilder::formatFieldError)
                .collect(Collectors.joining(", "));
    }

    private static String formatFieldError(FieldError fieldError) {
        return fieldError.getField() + ": " + fieldError.getDefaultMessage();
    }
}
