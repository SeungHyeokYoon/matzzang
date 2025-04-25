package com.matzzangteam.matzzang.dto.restaurant;

import com.matzzangteam.matzzang.entity.Restaurant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RestaurantRegisterRequest(
        @NotBlank String name,
        @NotBlank String address,
        Double latitude,
        Double longitude,
        @NotNull Restaurant.RestaurantCategory category,
        String description,
        String imageUrl
) {
}
