package com.matzzangteam.matzzang.service;

import com.matzzangteam.matzzang.dto.friendship.FriendListResponse;
import com.matzzangteam.matzzang.dto.restaurant.RestaurantDetailResponse;
import com.matzzangteam.matzzang.dto.restaurant.RestaurantRegisterRequest;
import com.matzzangteam.matzzang.dto.restaurant.RestaurantSearchRequest;
import com.matzzangteam.matzzang.entity.Restaurant;
import com.matzzangteam.matzzang.entity.Stamp;
import com.matzzangteam.matzzang.entity.User;
import com.matzzangteam.matzzang.exception.ClientErrorException;
import com.matzzangteam.matzzang.repository.FriendshipRepository;
import com.matzzangteam.matzzang.repository.RestaurantRepository;
import com.matzzangteam.matzzang.repository.StampRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final StampRepository stampRepository;
    private final FriendshipService friendshipService;

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

    @Transactional(readOnly = true)
    public RestaurantDetailResponse getRestaurantDetail(User user, Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ClientErrorException(HttpStatus.NOT_FOUND, "Restaurant not found"));

        List<Long> friendlist = friendshipService.getFriends(user).stream()
                .map(FriendListResponse::userId).collect(Collectors.toList());
        friendlist.add(user.getId());

        List<Stamp> stamps = stampRepository.findStampsByRestaurantAndUsers(restaurant.getId(), friendlist);

        List<RestaurantDetailResponse.StampInfo> stampInfos = stamps.stream()
                .map(stamp -> RestaurantDetailResponse.StampInfo.builder()
                        .challengeId(stamp.getChallenge().getId())
                        .challengeTitle(stamp.getChallenge().getTitle())
                        .visitedAt(stamp.getVisitedAt())
                        .imageUrl(stamp.getImageUrl())
                        .memo(stamp.getMemo())
                        .rate(stamp.getRate())
                        .build())
                .toList();

        return RestaurantDetailResponse.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .address(restaurant.getAddress())
                .latitude(restaurant.getLatitude())
                .longitude(restaurant.getLongitude())
                .category(restaurant.getCategory())
                .stamps(stampInfos)
                .build();
    }
}
