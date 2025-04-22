package com.matzzangteam.matzzang.dto.user;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
