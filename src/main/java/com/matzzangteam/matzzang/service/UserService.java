package com.matzzangteam.matzzang.service;

import com.matzzangteam.matzzang.dto.user.JoinRequest;
import com.matzzangteam.matzzang.dto.user.LoginRequest;
import com.matzzangteam.matzzang.dto.user.MyInfoResponse;
import com.matzzangteam.matzzang.entity.User;
import com.matzzangteam.matzzang.exception.ClientErrorException;
import com.matzzangteam.matzzang.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("No User for Email: " + email)
                );
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ClientErrorException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public User register(JoinRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new ClientErrorException(HttpStatus.BAD_REQUEST, "Email already Exist");
        }

        if (!request.password().equals(request.passwordCheck())) {
            throw new ClientErrorException(HttpStatus.BAD_REQUEST, "Password not match");
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

    public User login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() ->
                        new ClientErrorException(HttpStatus.UNAUTHORIZED, "Email not Exist")
                );

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new ClientErrorException(HttpStatus.UNAUTHORIZED, "Password not correct");
        }

        return user;
    }

    public MyInfoResponse getMyInfo(User user) {
        return new MyInfoResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getInviteCode(),
                user.getProfileImageUrl(),
                5,   // TODO: 실제 challenge 수
                12,  // TODO: 실제 방문 맛집 수
                3    // TODO: 실제 친구 수
        );
    }
}
