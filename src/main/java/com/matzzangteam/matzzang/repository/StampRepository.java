package com.matzzangteam.matzzang.repository;

import com.matzzangteam.matzzang.entity.Challenge;
import com.matzzangteam.matzzang.entity.Stamp;
import com.matzzangteam.matzzang.repository.custom.StampRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StampRepository extends JpaRepository<Stamp, Long>, StampRepositoryCustom {
    List<Stamp> findAllByChallenge(Challenge challenge);

    void deleteAllByChallenge(Challenge challenge);
}
