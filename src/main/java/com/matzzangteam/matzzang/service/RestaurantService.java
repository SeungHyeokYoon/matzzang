package com.matzzangteam.matzzang.service;

import com.matzzangteam.matzzang.dto.restaurant.RestaurantRegisterRequest;
import com.matzzangteam.matzzang.dto.restaurant.RestaurantSearchRequest;
import com.matzzangteam.matzzang.entity.Restaurant;
import com.matzzangteam.matzzang.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Transactional
    public void registerRestaurant(RestaurantRegisterRequest request) {
        Restaurant restaurant = Restaurant.builder()
                .name(request.name())
                .address(request.address())
                .latitude(request.latitude())
                .longitude(request.longitude())
                .category(request.category())
                .description(request.description())
                .imageUrl(request.imageUrl())
                .build();

        restaurantRepository.save(restaurant);
    }

    public Page<Restaurant> searchRestaurants(RestaurantSearchRequest request) {
        int page = request.page() != null ? request.page() : 0;
        int size = request.size() != null ? request.size() : 10;

        PageRequest pageable = PageRequest.of(page, size);

        return restaurantRepository.search(request, pageable);
    }
}
