package site.paymemory.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import site.paymemory.domain.user.dto.response.UserInfoResponse;
import site.paymemory.domain.user.service.UserService;
import site.paymemory.global.response.ResponseBody;

@Tag(name = "User", description = "사용자 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "내 정보 조회",
            description = "현재 로그인한 사용자의 기본 회원 정보를 조회합니다."
    )
    @ApiResponse(responseCode = "200", description = "내 정보 조회 성공")
    @ApiResponse(responseCode = "401", description = "인증 토큰 누락 또는 만료")
    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    @GetMapping("/me")
    public ResponseBody<UserInfoResponse> getMe(
            @AuthenticationPrincipal Long userId
    ) {

        UserInfoResponse response = userService.findMe(userId);

        return ResponseBody.success(response);
    }
}