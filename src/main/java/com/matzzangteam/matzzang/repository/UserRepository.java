package com.matzzangteam.matzzang.repository;

import com.matzzangteam.matzzang.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    
    boolean existsByEmail(String email);

    boolean existsByInviteCode(String inviteCode);

    Optional<User> findByEmail(String email);

    Optional<User> findByInviteCode(String inviteCode);
}
