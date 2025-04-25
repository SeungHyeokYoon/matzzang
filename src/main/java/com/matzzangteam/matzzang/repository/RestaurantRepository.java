package com.matzzangteam.matzzang.repository;

import com.matzzangteam.matzzang.entity.Restaurant;
import com.matzzangteam.matzzang.repository.custom.RestaurantRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long>, RestaurantRepositoryCustom {
}
