package com.matzzangteam.matzzang.dto.challenge;

import java.time.LocalDateTime;

public record StampResponse(
        Long stampId,
        Long restaurantId,
        String restaurantName,
        LocalDateTime visitedAt,
        String memo,
        Integer rate,
        String imageUrl
) {
}
