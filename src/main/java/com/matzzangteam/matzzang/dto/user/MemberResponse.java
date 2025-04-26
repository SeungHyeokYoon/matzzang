package com.matzzangteam.matzzang.dto.user;

public record MemberResponse(
        Long userId,
        String nickname,
        String profileImageUrl
) {
}
