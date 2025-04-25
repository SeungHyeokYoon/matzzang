package com.matzzangteam.matzzang.repository.custom;

import com.matzzangteam.matzzang.dto.restaurant.RestaurantSearchRequest;
import com.matzzangteam.matzzang.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RestaurantRepositoryCustom {
    Page<Restaurant> search(RestaurantSearchRequest condition, Pageable pageable);
}
