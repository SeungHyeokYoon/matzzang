package com.matzzangteam.matzzang.controller;

import com.matzzangteam.matzzang.dto.challenge.*;
import com.matzzangteam.matzzang.entity.User;
import com.matzzangteam.matzzang.service.ChallengeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/challenges")
@RequiredArgsConstructor
public class ChallengeController {

    private final ChallengeService challengeService;

    @PostMapping
    public ResponseEntity<Void> registerChallenge(@AuthenticationPrincipal User user,
                                                  @RequestBody @Valid ChallengeRegisterRequest request) {
        challengeService.registerChallenge(user, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/stamp")
    public ResponseEntity<Void> updateStamp(@AuthenticationPrincipal User user,
                                            @RequestBody @Valid StampUpdateRequest request) {
        challengeService.updateStamp(user, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{challengeId}")
    public ResponseEntity<ChallengeDetailResponse> getChallengeDetail(@PathVariable Long challengeId) {
        //add: user auth
        ChallengeDetailResponse response = challengeService.getChallengeDetail(challengeId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my")
    public ResponseEntity<List<ChallengeSimpleResponse>> getMyChallenges(@AuthenticationPrincipal User user) {
        List<ChallengeSimpleResponse> challenges = challengeService.getMyChallenges(user);
        return ResponseEntity.ok(challenges);
    }

    @PatchMapping("/{challengeId}")
    public ResponseEntity<Void> updateChallenge(@AuthenticationPrincipal User user,
                                                @PathVariable Long challengeId,
                                                @RequestBody @Valid ChallengeModifyRequest request) {
        challengeService.updateChallenge(user, challengeId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/stamp/{id}")
    public ResponseEntity<Void> deleteStamp(@AuthenticationPrincipal User user,
                                            @PathVariable Long id) {
        challengeService.deleteStamp(user, id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{challengeId}")
    public ResponseEntity<Void> deleteChallenge(@AuthenticationPrincipal User user,
                                                @PathVariable Long challengeId) {
        challengeService.deleteChallenge(user, challengeId);
        return ResponseEntity.ok().build();
    }
}
