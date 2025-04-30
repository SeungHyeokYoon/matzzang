package com.matzzangteam.matzzang.dto.challenge;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.List;

@Builder
public record ChallengeModifyRequest(
        @NotBlank String title,
        @NotBlank String description,
        List<Long> memberIds,
        List<Long> restaurantIds
) {
}
