package com.matzzangteam.matzzang.dto.restaurant;

import com.matzzangteam.matzzang.entity.Restaurant;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record RestaurantDetailResponse(
        Long id,
        String name,
        String address,
        Double latitude,
        Double longitude,
        Restaurant.RestaurantCategory category,
        List<StampInfo> stamps
) {

    @Builder
    public record StampInfo(
            Long challengeId,
            String challengeTitle,
            LocalDateTime visitedAt,
            String imageUrl,
            String memo,
            Integer rate
    ) {
    }
}
