package site.paymemory.global.security.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRedisRepository {

    private static final String REFRESH_TOKEN_KEY_PREFIX = "auth:refresh-token:";

    private final StringRedisTemplate stringRedisTemplate;

    @Value("${jwt.refresh-token-ttl}")
    private long refreshTokenTtl;

    public void saveRefreshToken(String hashedRefreshToken, Long userId) {

        stringRedisTemplate
                .opsForValue()
                .set(createKey(hashedRefreshToken), String.valueOf(userId), Duration.ofSeconds(refreshTokenTtl));
    }

    public Optional<Long> findUserIdByRefreshToken(String hashedRefreshToken) {

        String userId = stringRedisTemplate
                .opsForValue()
                .get(createKey(hashedRefreshToken));

        if (userId == null) {

            return Optional.empty();
        }

        return Optional.of(Long.valueOf(userId));
    }

    public void deleteRefreshToken(String hashedRefreshToken) {

        stringRedisTemplate.delete(createKey(hashedRefreshToken));
    }

    private String createKey(String hashedRefreshToken) {

        return REFRESH_TOKEN_KEY_PREFIX + hashedRefreshToken;
    }
}