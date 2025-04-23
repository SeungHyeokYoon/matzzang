package com.matzzangteam.matzzang.dto.user;

public record MyInfoResponse(
        Long id,
        String email,
        String nickname,
        String inviteCode,
        String profileImageUrl,
        int challengeCount,
        int visitedRestaurantCount,
        int friendCount
) {
}
