package com.matzzangteam.matzzang.dto.restaurant;

import com.matzzangteam.matzzang.entity.Restaurant;

public record RestaurantSearchRequest(
        String name,
        Restaurant.RestaurantCategory category,
        Integer page,
        Integer size
) {
}
