package com.matzzangteam.matzzang.repository;

import com.matzzangteam.matzzang.entity.Challenge;
import com.matzzangteam.matzzang.entity.Stamp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StampRepository extends JpaRepository<Stamp, Long> {
    List<Stamp> findAllByChallenge(Challenge challenge);
}
