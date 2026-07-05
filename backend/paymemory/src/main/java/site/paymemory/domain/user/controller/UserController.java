package site.paymemory.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.paymemory.domain.user.dto.response.UserInfoResponse;
import site.paymemory.domain.user.service.UserService;
import site.paymemory.global.response.ResponseBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseBody<UserInfoResponse> getMe(
            @AuthenticationPrincipal Long userId
    ) {
        UserInfoResponse response = userService.findMe(userId);

        return ResponseBody.success(response);
    }
}
