package com.matzzangteam.matzzang.dto.friendship;

import jakarta.validation.constraints.NotBlank;

public record FriendRequest(
        @NotBlank(message = "초대 코드는 필수입니다.")
        String inviteCode
) {
}
