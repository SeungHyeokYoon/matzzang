package com.matzzangteam.matzzang.dto.restaurant;

import com.matzzangteam.matzzang.entity.Restaurant;

public record RestaurantSearchResponse(
        Long id,
        String name,
        String address,
        Double latitude,
        Double longitude,
        String imageUrl,
        Restaurant.RestaurantCategory category,
        String description
) {
    public static RestaurantSearchResponse from(Restaurant restaurant) {
        return new RestaurantSearchResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getLatitude(),
                restaurant.getLongitude(),
                restaurant.getImageUrl(),
                restaurant.getCategory(),
                restaurant.getDescription()
        );
    }
}
