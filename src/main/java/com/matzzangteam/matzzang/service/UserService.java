package com.matzzangteam.matzzang.service;

import com.matzzangteam.matzzang.dto.JoinRequest;
import com.matzzangteam.matzzang.entity.User;
import com.matzzangteam.matzzang.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User register(JoinRequest request){
        String inviteCode = generateInviteCode();
        User user = User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .nickname(request.getNickname())
                .inviteCode(inviteCode)
                .build();
        return userRepository.save(user);
    }

    private String generateInviteCode() {
        return UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}
