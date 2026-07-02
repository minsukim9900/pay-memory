package site.paymemory.global.security.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
public class CookieProvider {

    private final String accessTokenName;
    private final long accessTokenTtl;
    private final String refreshTokenName;
    private final long refreshTokenTtl;
    private final String cookieDomain;

    public CookieProvider(
            @Value("${jwt.access-token-name}") String accessTokenName,
            @Value("${jwt.access-token-ttl}") long accessTokenTtl,
            @Value("${jwt.refresh-token-name}") String refreshTokenName,
            @Value("${jwt.refresh-token-ttl}") long refreshTokenTtl,
            @Value("${cookie.domain}") String cookieDomain
    ) {

        this.accessTokenName = accessTokenName;
        this.accessTokenTtl = accessTokenTtl;
        this.refreshTokenName = refreshTokenName;
        this.refreshTokenTtl = refreshTokenTtl;
        this.cookieDomain = cookieDomain;
    }

    public void addAccessTokenCookie(HttpServletResponse response, String accessToken) {

        ResponseCookie cookie = ResponseCookie
                .from(accessTokenName, accessToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .domain(cookieDomain)
                .path("/")
                .maxAge(accessTokenTtl)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    public void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {

        ResponseCookie cookie = ResponseCookie
                .from(refreshTokenName, refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .domain(cookieDomain)
                .path("/")
                .maxAge(refreshTokenTtl)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    public Optional<String> getRefreshToken(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();

        if (cookies == null) {

            return Optional.empty();
        }

        return Arrays.stream(cookies)
                .filter(cookie -> refreshTokenName.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }
}