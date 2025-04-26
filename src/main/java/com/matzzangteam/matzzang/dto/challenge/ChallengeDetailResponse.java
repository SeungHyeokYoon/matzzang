package com.matzzangteam.matzzang.dto.challenge;

import com.matzzangteam.matzzang.dto.user.MemberResponse;

import java.util.List;

public record ChallengeDetailResponse(
        Long id,
        String title,
        String description,
        List<MemberResponse> members,
        List<StampResponse> stamps
) {
}
