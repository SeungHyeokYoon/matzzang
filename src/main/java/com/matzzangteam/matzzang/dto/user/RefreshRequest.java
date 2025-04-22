package com.matzzangteam.matzzang.dto.user;

public record RefreshRequest(
        Long userId,
        String refreshToken
) {
}
