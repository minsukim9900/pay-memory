package site.paymemory.domain.transaction.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record UploadTransactionExcelRequest(
        Long userId,
        MultipartFile file,
        String filePassword
) {

    public static UploadTransactionExcelRequest of(
            Long userId,
            MultipartFile file,
            String filePassword
    ) {

        return new UploadTransactionExcelRequest(
                userId,
                file,
                filePassword
        );
    }
}