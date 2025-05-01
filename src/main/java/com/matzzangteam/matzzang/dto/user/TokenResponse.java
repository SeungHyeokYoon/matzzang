package com.matzzangteam.matzzang.dto.user;

public record TokenResponse(
        Long userId,
        String accessToken,
        String refreshToken
) {
}
