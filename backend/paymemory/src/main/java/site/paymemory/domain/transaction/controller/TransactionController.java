package site.paymemory.domain.transaction.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import site.paymemory.domain.transaction.dto.request.UploadTransactionExcelRequest;
import site.paymemory.domain.transaction.dto.response.UploadTransactionExcelResponse;
import site.paymemory.domain.transaction.service.TransactionExcelUploadService;
import site.paymemory.global.response.ResponseBody;

@Tag(name = "Transaction", description = "거래내역 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionExcelUploadService transactionExcelUploadService;

    @Operation(
            summary = "카카오페이 거래내역 엑셀 업로드",
            description = "카카오페이 거래내역 엑셀 파일을 업로드하고 거래내역을 저장합니다. 비밀번호가 설정된 엑셀 파일은 filePassword를 함께 전달해야 합니다."
    )
    @ApiResponse(responseCode = "200", description = "거래내역 엑셀 업로드 성공")
    @ApiResponse(responseCode = "400", description = "엑셀 파일 누락, 잘못된 파일 형식, 비밀번호 누락 또는 파싱 실패")
    @ApiResponse(responseCode = "401", description = "인증 토큰 누락 또는 만료")
    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    @PostMapping(
            value = "/excel",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseBody<UploadTransactionExcelResponse> uploadTransactionExcel(
            @AuthenticationPrincipal Long userId,

            @Parameter(description = "업로드할 카카오페이 거래내역 엑셀 파일")
            @RequestPart("file") MultipartFile file,

            @Parameter(description = "엑셀 파일 비밀번호")
            @RequestPart(value = "filePassword", required = false) String filePassword
    ) {

        UploadTransactionExcelRequest request = UploadTransactionExcelRequest.of(
                userId,
                file,
                filePassword
        );

        UploadTransactionExcelResponse response = transactionExcelUploadService.upload(request);

        return ResponseBody.success(response);
    }
}