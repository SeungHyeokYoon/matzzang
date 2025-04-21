package com.matzzangteam.matzzang.dto.user;

public record JoinResponse(
        Long id,
        String email,
        String nickname,
        String inviteCode
) {
}
