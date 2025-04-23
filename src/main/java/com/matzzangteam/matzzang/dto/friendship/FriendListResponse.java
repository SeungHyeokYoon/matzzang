package com.matzzangteam.matzzang.dto.friendship;

public record FriendListResponse(
        Long userId,
        String nickname,
        String profileImageUrl
) {
}
