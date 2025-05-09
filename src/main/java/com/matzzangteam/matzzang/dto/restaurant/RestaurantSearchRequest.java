package com.matzzangteam.matzzang.dto.restaurant;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.matzzangteam.matzzang.entity.Restaurant;

public record RestaurantSearchRequest(
        String name,
        Restaurant.RestaurantCategory category,
        Integer page,
        Integer size
) {
}
