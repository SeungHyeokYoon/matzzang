package com.matzzangteam.matzzang.repository.custom;

import com.matzzangteam.matzzang.entity.Stamp;
import com.matzzangteam.matzzang.entity.User;

import java.util.List;

public interface StampRepositoryCustom {
    List<Stamp> findStampsByRestaurantAndUsers(Long restaurantId, List<Long> userIds);

    int countVisitedRestaurantsByUser(User user);
}
