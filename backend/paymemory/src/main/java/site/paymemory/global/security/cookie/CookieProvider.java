package site.paymemory.global.security.cookie;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieProvider {

    private final String accessTokenName;
    private final long accessTokenTtl;
    private final String cookieDomain;

    public CookieProvider(
            @Value("${jwt.access-token-name}") String accessTokenName,
            @Value("${jwt.access-token-ttl}") long accessTokenTtl,
            @Value("${cookie.domain}") String cookieDomain
    ) {

        this.accessTokenName = accessTokenName;
        this.accessTokenTtl = accessTokenTtl;
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
}