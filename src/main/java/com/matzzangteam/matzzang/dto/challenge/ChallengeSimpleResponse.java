package com.matzzangteam.matzzang.dto.challenge;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChallengeSimpleResponse(
        Long challengeId,
        String title,
        String description,
        LocalDateTime createdAt,
        int memberCount,
        int restaurantCount,
        int completedCount,
        double progress
) {
}
