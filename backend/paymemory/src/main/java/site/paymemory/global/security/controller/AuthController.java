package site.paymemory.global.security.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.paymemory.global.response.ResponseBody;
import site.paymemory.global.security.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/reissue")
    public ResponseBody<Void> reissueToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        authService.reissueToken(request, response);

        return ResponseBody.success();
    }
}