package site.paymemory.global.security.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.paymemory.global.exception.GlobalException;
import site.paymemory.global.security.cookie.CookieProvider;
import site.paymemory.global.security.exception.TokenErrorCode;
import site.paymemory.global.security.jwt.TokenProvider;
import site.paymemory.global.security.provider.RefreshTokenProvider;
import site.paymemory.global.security.repository.RefreshTokenRedisRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenProvider refreshTokenProvider;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final CookieProvider cookieProvider;

    public void reissueToken(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = cookieProvider.getRefreshToken(request)
                .orElseThrow(() -> new GlobalException(TokenErrorCode.REFRESH_TOKEN_COOKIE_NOT_FOUND));

        String hashedRefreshToken = refreshTokenProvider.hashRefreshToken(refreshToken);

        Long userId = refreshTokenRedisRepository.findUserIdByRefreshToken(hashedRefreshToken)
                .orElseThrow(() -> new GlobalException(TokenErrorCode.INVALID_REFRESH_TOKEN));

        String newAccessToken = tokenProvider.generateAccessToken(userId);
        String newRefreshToken = refreshTokenProvider.generateRefreshToken();
        String newHashedRefreshToken = refreshTokenProvider.hashRefreshToken(newRefreshToken);

        refreshTokenRedisRepository.deleteRefreshToken(hashedRefreshToken);
        refreshTokenRedisRepository.saveRefreshToken(newHashedRefreshToken, userId);

        cookieProvider.addAccessTokenCookie(response, newAccessToken);
        cookieProvider.addRefreshTokenCookie(response, newRefreshToken);
    }
}