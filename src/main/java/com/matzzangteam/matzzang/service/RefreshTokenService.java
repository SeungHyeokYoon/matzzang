package com.matzzangteam.matzzang.service;

import com.matzzangteam.matzzang.dto.user.LoginRequest;
import com.matzzangteam.matzzang.dto.user.RefreshRequest;
import com.matzzangteam.matzzang.dto.user.TokenResponse;
import com.matzzangteam.matzzang.entity.RefreshToken;
import com.matzzangteam.matzzang.entity.User;
import com.matzzangteam.matzzang.exception.ClientErrorException;
import com.matzzangteam.matzzang.jwt.JwtProvider;
import com.matzzangteam.matzzang.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    private void save(Long userId, String token, long expirationMillis) {
        refreshTokenRepository.deleteByUserId(userId);

        LocalDateTime expirationTime = LocalDateTime.now().plusNanos(expirationMillis * 1_000_000);
        refreshTokenRepository.save(
                RefreshToken.builder()
                        .userId(userId)
                        .token(token)
                        .expiration(expirationTime)
                        .build()
        );
    }

    @Transactional
    public TokenResponse loginAndGenerateTokens(LoginRequest request) {
        User user = userService.login(request);

        String accessToken = jwtProvider.createAccessToken(user.getId(), user.getEmail());
        String refreshToken = jwtProvider.createRefreshToken(user.getEmail());

        save(user.getId(), refreshToken, refreshTokenExpiration);

        return new TokenResponse(user.getId(), accessToken, refreshToken);
    }

    @Transactional
    public TokenResponse reissueTokens(RefreshRequest request) {
        RefreshToken savedToken = refreshTokenRepository.findByUserId(request.userId())
                .orElseThrow(() -> new ClientErrorException(HttpStatus.UNAUTHORIZED, "No Refresh Token"));

//        if (!passwordEncoder.matches(request.refreshToken(), savedToken.getToken())) {
//            throw new ClientErrorException(HttpStatus.UNAUTHORIZED, "Refresh Token not Match");
//        }

        if (!savedToken.getToken().equals(request.refreshToken())) {
            throw new ClientErrorException(HttpStatus.UNAUTHORIZED, "Refresh Token not Match");
        }

        if (savedToken.getExpiration().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.deleteByUserId(request.userId());
            throw new ClientErrorException(HttpStatus.UNAUTHORIZED, "Refresh Token Expired");
        }

        String email = jwtProvider.getEmailFromToken(request.refreshToken());
        String newAccessToken = jwtProvider.createAccessToken(request.userId(), email);
        String newRefreshToken = jwtProvider.createRefreshToken(email);

        save(request.userId(), newRefreshToken, refreshTokenExpiration);

        return new TokenResponse(request.userId(), newAccessToken, newRefreshToken);
    }

}
