package com.matzzangteam.matzzang.controller;

import com.matzzangteam.matzzang.dto.friendship.FriendAcceptRequest;
import com.matzzangteam.matzzang.dto.friendship.FriendRequest;
import com.matzzangteam.matzzang.dto.friendship.FriendListResponse;
import com.matzzangteam.matzzang.entity.User;
import com.matzzangteam.matzzang.service.FriendshipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendshipController {

    private final FriendshipService friendshipService;

    @PostMapping("/request")
    public ResponseEntity<Void> sendFriendRequest(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid FriendRequest request
    ) {
        friendshipService.sendFriendRequest(user, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/requests")
    public ResponseEntity<List<FriendListResponse>> getRequests(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(friendshipService.getReceivedRequests(user));
    }

    @PostMapping("/accept")
    public ResponseEntity<Void> acceptFriendRequest(@AuthenticationPrincipal User user,
                                                    @RequestBody @Valid FriendAcceptRequest request) {
        friendshipService.acceptFriendRequest(user, request.userId());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<FriendListResponse>> getFriendList(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(friendshipService.getFriends(user));
    }
}
