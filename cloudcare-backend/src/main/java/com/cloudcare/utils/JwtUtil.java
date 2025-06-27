package com.cloudcare.utils;

import com.cloudcare.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 *
 * @author cloudcare
 */
@Slf4j
@Component
@Lazy
public class JwtUtil {

    @Value("${cloudcare.jwt.secret}")
    private String secret;

    @Value("${cloudcare.jwt.expiration}")
    private Long expiration;

    /**
     * 生成JWT令牌
     *
     * @param user 用户信息
     * @return JWT令牌
     */
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>(4);
        claims.put("userId", user.getUserId());
        claims.put("username", user.getUsername());
        claims.put("userType", user.getUserType());
        return generateToken(claims);
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.get("username", String.class);
        } catch (Exception e) {
            username = null;
            log.error("从JWT令牌中获取用户名失败", e);
        }
        return username;
    }

    /**
     * 从令牌中获取用户ID
     *
     * @param token 令牌
     * @return 用户ID
     */
    public Long getUserIdFromToken(String token) {
        Long userId;
        try {
            Claims claims = getClaimsFromToken(token);
            userId = claims.get("userId", Long.class);
        } catch (Exception e) {
            userId = null;
            log.error("从JWT令牌中获取用户ID失败", e);
        }
        return userId;
    }

    /**
     * 验证令牌
     *
     * @param token       令牌
     * @param userDetails 用户信息
     * @return 是否有效
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return username != null && username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * 判断令牌是否过期
     *
     * @param token 令牌
     * @return 是否过期
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            log.error("判断JWT令牌是否过期失败", e);
            return true;
        }
    }

    /**
     * 刷新令牌
     *
     * @param token 原令牌
     * @return 新令牌
     */
    public String refreshToken(String token) {
        String refreshedToken;
        try {
            Claims claims = getClaimsFromToken(token);
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
            log.error("刷新JWT令牌失败", e);
        }
        return refreshedToken;
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String generateToken(Map<String, Object> claims) {
        Date expirationDate = new Date(System.currentTimeMillis() + expiration * 60 * 1000);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 获取密钥
     *
     * @return 密钥
     */
    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}