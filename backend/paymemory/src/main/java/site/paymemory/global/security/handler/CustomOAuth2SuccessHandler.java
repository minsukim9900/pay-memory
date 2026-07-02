package site.paymemory.global.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import site.paymemory.global.security.cookie.CookieProvider;
import site.paymemory.global.security.jwt.TokenProvider;
import site.paymemory.global.security.oauth.CustomOAuth2User;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final CookieProvider cookieProvider;

    @Value("${oauth2.success-redirect-url}")
    private String successRedirectUrl;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String accessToken = tokenProvider.generateAccessToken(customOAuth2User.getUserId());

        cookieProvider.addAccessTokenCookie(response, accessToken);

        response.sendRedirect(successRedirectUrl);
    }
}