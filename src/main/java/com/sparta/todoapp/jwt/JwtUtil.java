package com.sparta.todoapp.jwt;

import com.sparta.todoapp.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class JwtUtil {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_TOKEN = "X-REFRESH-TOKEN";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String BEARER_PREFIX = "Bearer ";
    private final long TOKEN_TIME = 60 * 60 * 1000L; // 1 hour
    private final long REFRESH_TOKEN_TIME = 60 * 60 * 1000 * 24L;

    @Value("${jwt.secret.key}")
    private String secretKey;

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    private Key key;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createToken(Member member) {
        Date expireDate = createExpireDate(TOKEN_TIME);

        return BEARER_PREFIX +
            Jwts.builder()
                .setSubject(member.getUsername())
                .setExpiration(expireDate)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    public String createRefreshToken(Member member) {
        Date expireDate = createExpireDate(REFRESH_TOKEN_TIME);

        return Jwts.builder()
            .setSubject(member.getUsername())
            .setExpiration(expireDate)
            .signWith(key, signatureAlgorithm)
            .compact();
    }

    // 토큰 뽑아오기
    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    private Date createExpireDate(long expireDate) {
        long curTime = (new Date()).getTime();
        return new Date(curTime + expireDate);
    }

    private Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();

        header.put("typ", "ACCESS_TOKEN");
        header.put("alg", "HS256");
        header.put("redDate", System.currentTimeMillis());

        return header;
    }

    private Map<String, Object> createClaims(Member member) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(AUTHORIZATION_KEY, member.getId());

        return claims;
    }

    public Claims getClaimsFormToken(String accessToken) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(accessToken)
            .getBody();
    }

//    후에 AccessToken 과 RefreshToken 의 key 값을 공유하지 않을 가능성을 고려하여 메서드 분리

    public Claims getClaimsFormRefreshToken(String refreshToken) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(refreshToken)
            .getBody();
    }

}
