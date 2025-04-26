package com.matzzangteam.matzzang.dto.challenge;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ChallengeRegisterRequest(
        @NotBlank String title,
        @NotBlank String description,
        @NotEmpty List<Long> memberIds,
        @NotEmpty List<Long> restaurantIds
) {
}
