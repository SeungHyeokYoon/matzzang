package com.matzzangteam.matzzang.dto.friendship;

import jakarta.validation.constraints.NotNull;

public record FriendAcceptRequest(
        @NotNull(message = "userId는 필수입니다.")
        Long userId
) {
}
