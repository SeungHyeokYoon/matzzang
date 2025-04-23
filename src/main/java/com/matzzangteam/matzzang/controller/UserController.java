package com.matzzangteam.matzzang.controller;

import com.matzzangteam.matzzang.dto.user.JoinRequest;
import com.matzzangteam.matzzang.dto.user.JoinResponse;
import com.matzzangteam.matzzang.dto.user.MyInfoResponse;
import com.matzzangteam.matzzang.entity.User;
import com.matzzangteam.matzzang.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<JoinResponse> join(@RequestBody @Valid JoinRequest request) {
        User user = userService.register(request);
        return ResponseEntity.ok().body(
                new JoinResponse(
                        user.getId(),
                        user.getEmail(),
                        user.getNickname(),
                        user.getInviteCode()
                )
        );
    }

    @GetMapping("/me")
    public ResponseEntity<MyInfoResponse> getMyInfo(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.getMyInfo(user));
    }
}
