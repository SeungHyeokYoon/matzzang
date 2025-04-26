package com.matzzangteam.matzzang.repository;

import com.matzzangteam.matzzang.entity.Challenge;
import com.matzzangteam.matzzang.entity.ChallengeMember;
import com.matzzangteam.matzzang.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ChallengeMemberRepository extends JpaRepository<ChallengeMember, Long> {

    List<ChallengeMember> findAllByChallenge(Challenge challenge);

    int countByChallenge(Challenge challenge);

    List<ChallengeMember> findAllByUser(User user);
}
