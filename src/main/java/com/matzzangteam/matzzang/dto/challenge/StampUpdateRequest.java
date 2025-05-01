package com.matzzangteam.matzzang.dto.challenge;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record StampUpdateRequest(
        @NotNull
        Long stampId,
        LocalDateTime visitedAt,
        String memo,
        @NotNull(message = "Rate is necessary")
        @Min(1) @Max(5)
        Integer rate,
        String imageUrl
) {
}
