package com.matzzangteam.matzzang.service;

import com.matzzangteam.matzzang.dto.user.JoinRequest;
import com.matzzangteam.matzzang.entity.User;
import com.matzzangteam.matzzang.exception.ClientErrorException;
import com.matzzangteam.matzzang.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public User register(JoinRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new ClientErrorException(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다.");
        }

        if (!request.password().equals(request.passwordCheck())) {
            throw new ClientErrorException(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
        }

        String inviteCode = generateUniqueInviteCode();

        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .nickname(request.nickname())
                .inviteCode(inviteCode)
                .build();

        return userRepository.save(user);
    }

    private String generateUniqueInviteCode() {
        String code;
        do {
            code = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        } while (userRepository.existsByInviteCode(code));
        return code;
    }
}
