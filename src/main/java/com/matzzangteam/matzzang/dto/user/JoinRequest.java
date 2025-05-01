package com.matzzangteam.matzzang.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record JoinRequest(
        @Email(message = "Invalid Email")
        @NotBlank(message = "Email is necessary")
        String email,

        @NotBlank(message = "Nickname is necessary")
        String nickname,

        @NotBlank(message = "Password is necessary")
        String password,

        @NotBlank(message = "Password validation is necessary")
        String passwordCheck
) {
}
