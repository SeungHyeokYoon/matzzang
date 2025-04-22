package com.matzzangteam.matzzang.jwt;

import com.matzzangteam.matzzang.exception.ClientErrorException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(final Long userId, final String email) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessTokenExpiration);

        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(final String email) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + refreshTokenExpiration);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(final String token) {
        if (token == null || token.isBlank()) {
            throw new ClientErrorException(HttpStatus.UNAUTHORIZED, "No JWT Token");
        }

        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw new ClientErrorException(HttpStatus.UNAUTHORIZED, "Invalid JWT Token");
        } catch (ExpiredJwtException e) {
            throw new ClientErrorException(HttpStatus.UNAUTHORIZED, "Expired JWT Token");
        } catch (UnsupportedJwtException e) {
            throw new ClientErrorException(HttpStatus.UNAUTHORIZED, "Unsupported JWT Token");
        } catch (IllegalArgumentException e) {
            throw new ClientErrorException(HttpStatus.UNAUTHORIZED, "JWT claims string is empty");
        }
    }

    public Claims getClaims(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getEmailFromToken(final String token) {
        return getClaims(token).getSubject();
    }

    public Date getExpiration(final String token) {
        return getClaims(token).getExpiration();
    }
}
