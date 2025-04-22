package com.matzzangteam.matzzang.controller;

import com.matzzangteam.matzzang.dto.user.LoginRequest;
import com.matzzangteam.matzzang.dto.user.RefreshRequest;
import com.matzzangteam.matzzang.dto.user.TokenResponse;
import com.matzzangteam.matzzang.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RefreshTokenService refreshTokenService;


    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(refreshTokenService.loginAndGenerateTokens(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody RefreshRequest request) {
        return ResponseEntity.ok(refreshTokenService.reissueTokens(request));
    }
}
