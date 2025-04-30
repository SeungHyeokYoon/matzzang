package com.matzzangteam.matzzang.controller;

import com.matzzangteam.matzzang.dto.restaurant.RestaurantDetailResponse;
import com.matzzangteam.matzzang.dto.restaurant.RestaurantRegisterRequest;
import com.matzzangteam.matzzang.dto.restaurant.RestaurantSearchRequest;
import com.matzzangteam.matzzang.dto.restaurant.RestaurantSearchResponse;
import com.matzzangteam.matzzang.entity.Restaurant;
import com.matzzangteam.matzzang.entity.User;
import com.matzzangteam.matzzang.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<Void> register(@RequestBody @Valid RestaurantRegisterRequest request) {
        restaurantService.registerRestaurant(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<RestaurantSearchResponse>> searchRestaurants(@RequestBody RestaurantSearchRequest request) {
        Page<Restaurant> restaurants = restaurantService.searchRestaurants(request);

        return ResponseEntity.ok(restaurants.stream()
                .map(RestaurantSearchResponse::from)
                .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDetailResponse> getRestaurantDetail(@AuthenticationPrincipal User user, @PathVariable Long id) {
        RestaurantDetailResponse response = restaurantService.getRestaurantDetail(user, id);
        return ResponseEntity.ok(response);
    }
}
