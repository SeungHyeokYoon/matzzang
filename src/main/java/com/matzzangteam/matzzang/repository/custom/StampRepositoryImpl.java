package com.matzzangteam.matzzang.repository.custom;

import com.matzzangteam.matzzang.entity.QChallengeMember;
import com.matzzangteam.matzzang.entity.QStamp;
import com.matzzangteam.matzzang.entity.Stamp;
import com.matzzangteam.matzzang.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class StampRepositoryImpl implements StampRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Stamp> findStampsByRestaurantAndUsers(Long restaurantId, List<Long> userIds) {
        QStamp stamp = QStamp.stamp;
        QChallengeMember challengeMember = QChallengeMember.challengeMember;

        return queryFactory
                .selectDistinct(stamp)
                .from(stamp)
                .join(challengeMember).on(stamp.challenge.id.eq(challengeMember.challenge.id))
                .where(
                        stamp.restaurant.id.eq(restaurantId),
                        challengeMember.user.id.in(userIds)
                )
                .fetch();
    }

    @Override
    public int countVisitedRestaurantsByUser(User user) {
        QStamp stamp = QStamp.stamp;
        QChallengeMember challengeMember = QChallengeMember.challengeMember;

        Long count = queryFactory
                .select(stamp.restaurant.id.countDistinct())
                .from(stamp)
                .join(challengeMember).on(stamp.challenge.id.eq(challengeMember.challenge.id))
                .where(
                        challengeMember.user.eq(user),
                        stamp.visitedAt.isNotNull()
                )
                .fetchOne();

        return count != null ? count.intValue() : 0;
    }
}
