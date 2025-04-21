package com.matzzangteam.matzzang.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record JoinRequest(
        @Email(message = "이메일 형식이 유효하지 않습니다.")
        @NotBlank(message = "이메일은 필수입니다.")
        String email,

        @NotBlank(message = "닉네임은 필수입니다.")
        String nickname,

        @NotBlank(message = "비밀번호는 필수입니다.")
        String password,

        @NotBlank(message = "비밀번호 확인은 필수입니다.")
        String passwordCheck
) {
}
