package com.matzzangteam.matzzang.repository;

import com.matzzangteam.matzzang.entity.Friendship;
import com.matzzangteam.matzzang.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    boolean existsByFromUserAndToUser(User fromUser, User toUser);

    @Query("""
                SELECT f FROM Friendship f
                WHERE 
                    (f.fromUser = :user1 AND f.toUser = :user2)
                    OR
                    (f.fromUser = :user2 AND f.toUser = :user1)
            """)
    Optional<Friendship> findFriendshipBetweenUsers(@Param("user1") User user1, @Param("user2") User user2);

    List<Friendship> findByToUserAndStatus(User toUser, Friendship.FriendshipStatus status);

    Optional<Friendship> findByFromUserIdAndToUserIdAndStatus(Long fromUserId, Long toUserId, Friendship.FriendshipStatus status);

    @Query("""
                SELECT f FROM Friendship f
                WHERE 
                    (f.fromUser = :user OR f.toUser = :user)
                    AND f.status = :status
            """)
    List<Friendship> findAllByUserAndStatus(@Param("user") User user,
                                            @Param("status") Friendship.FriendshipStatus status);
}
