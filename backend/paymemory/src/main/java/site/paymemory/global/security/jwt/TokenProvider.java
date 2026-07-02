package site.paymemory.global.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Component
public class TokenProvider {

    private final SecretKey secretKey;
    private final long accessTokenTtl;

    public TokenProvider(
            @Value("${jwt.key}") String key,
            @Value("${jwt.access-token-ttl}") long accessTokenTtl
    ) {

        this.secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
        this.accessTokenTtl = accessTokenTtl;
    }

    public String generateAccessToken(Long userId) {

        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(accessTokenTtl);

        return Jwts
                .builder()
                .subject(String.valueOf(userId))
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }
}