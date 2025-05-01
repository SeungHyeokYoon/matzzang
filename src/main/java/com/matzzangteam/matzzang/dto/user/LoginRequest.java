package com.matzzangteam.matzzang.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public record LoginRequest(
        @Email(message = "Invalid Email")
        @NotBlank(message = "Email is necessary")
        String email,

        @NotBlank(message = "Password is necessary")
        String password
) {
}
