package com.matzzangteam.matzzang.repository.custom;

import com.matzzangteam.matzzang.dto.restaurant.RestaurantSearchRequest;
import com.matzzangteam.matzzang.entity.QRestaurant;
import com.matzzangteam.matzzang.entity.Restaurant;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class RestaurantRepositoryImpl implements RestaurantRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Restaurant> search(RestaurantSearchRequest condition, Pageable pageable) {
        QRestaurant restaurant = QRestaurant.restaurant;
        BooleanBuilder builder = new BooleanBuilder();

        if (condition.name() != null && !condition.name().isBlank()) {
            builder.and(restaurant.name.containsIgnoreCase(condition.name()));
        }

        if (condition.category() != null) {
            builder.and(restaurant.category.eq(condition.category()));
        }

        JPAQuery<Restaurant> query = queryFactory
                .selectFrom(restaurant)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<Restaurant> content = query.fetch();

        Long countResult = queryFactory
                .select(restaurant.count())
                .from(restaurant)
                .where(builder)
                .fetchOne();

        long total = (countResult != null) ? countResult : 0L;

        return new PageImpl<>(content, pageable, total);
    }
}
