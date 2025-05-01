package com.matzzangteam.matzzang.dto.user;

public record UpdateUserRequest(
        String nickname,
        String profileImageUrl
) {
}
