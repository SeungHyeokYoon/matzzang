package com.matzzangteam.matzzang.dto.friendship;

import jakarta.validation.constraints.NotBlank;

public record FriendRequest(
        @NotBlank(message = "inviteCode is necessary")
        String inviteCode
) {
}
