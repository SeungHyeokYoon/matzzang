package com.matzzangteam.matzzang.dto.friendship;

import jakarta.validation.constraints.NotBlank;

public record FriendAcceptRequest(
        @NotBlank(message = "userId is necessary")
        Long userId
) {
}
