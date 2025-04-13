package com.matzzangteam.matzzang.controller;

import com.matzzangteam.matzzang.dto.JoinRequest;
import com.matzzangteam.matzzang.entity.User;
import com.matzzangteam.matzzang.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> join(@RequestBody @Valid JoinRequest request) {
        User user = userService.register(request);
        return ResponseEntity.ok("success");
    }
}
