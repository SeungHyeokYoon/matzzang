package com.matzzangteam.matzzang.service;

import com.matzzangteam.matzzang.dto.challenge.*;
import com.matzzangteam.matzzang.dto.user.MemberResponse;
import com.matzzangteam.matzzang.entity.*;
import com.matzzangteam.matzzang.exception.ClientErrorException;
import com.matzzangteam.matzzang.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final ChallengeMemberRepository challengeMemberRepository;
    private final StampRepository stampRepository;

    @Transactional
    public void registerChallenge(User creator, ChallengeRegisterRequest request) {

        Challenge challenge = Challenge.builder()
                .title(request.title())
                .description(request.description())
                .build();
        challengeRepository.save(challenge);


        List<Long> memberIds = request.memberIds();
        memberIds.add(creator.getId());


        List<User> members = userRepository.findAllById(memberIds);
        if (members.size() != memberIds.size()) {
            throw new ClientErrorException(HttpStatus.NOT_FOUND, "Some users were not found");
        }

        List<ChallengeMember> challengeMembers = members.stream()
                .map(user -> ChallengeMember.builder()
                        .challenge(challenge)
                        .user(user)
                        .build())
                .toList();

        challengeMemberRepository.saveAll(challengeMembers);


        List<Restaurant> restaurants = restaurantRepository.findAllById(request.restaurantIds());
        if (restaurants.size() != request.restaurantIds().size()) {
            throw new ClientErrorException(HttpStatus.NOT_FOUND, "Some restaurants were not found");
        }

        List<Stamp> stamps = restaurants.stream()
                .map(restaurant -> Stamp.builder()
                        .challenge(challenge)
                        .restaurant(restaurant)
                        .build())
                .toList();

        stampRepository.saveAll(stamps);
    }

    @Transactional
    public void updateStamp(StampUpdateRequest request) {
        Stamp stamp = stampRepository.findById(request.stampId())
                .orElseThrow(() -> new ClientErrorException(HttpStatus.NOT_FOUND, "Stamp not found"));

        //add: verify user that really has auth to fix

        stamp.setVisitedAt(request.visitedAt());
        stamp.setMemo(request.memo());
        stamp.setRate(request.rate());
        stamp.setImageUrl(request.imageUrl());
    }

    @Transactional(readOnly = true)
    public ChallengeDetailResponse getChallengeDetail(Long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new ClientErrorException(HttpStatus.NOT_FOUND, "Challenge not found"));

        List<MemberResponse> members = challengeMemberRepository.findAllByChallenge(challenge).stream()
                .map(cm -> new MemberResponse(
                        cm.getUser().getId(),
                        cm.getUser().getNickname(),
                        cm.getUser().getProfileImageUrl()
                ))
                .toList();

        List<StampResponse> stamps = stampRepository.findAllByChallenge(challenge).stream()
                .map(stamp -> new StampResponse(
                        stamp.getId(),
                        stamp.getRestaurant().getId(),
                        stamp.getRestaurant().getName(),
                        stamp.getVisitedAt(),
                        stamp.getMemo(),
                        stamp.getRate(),
                        stamp.getImageUrl()
                ))
                .toList();

        return new ChallengeDetailResponse(
                challenge.getId(),
                challenge.getTitle(),
                challenge.getDescription(),
                members,
                stamps
        );
    }

    @Transactional
    public List<ChallengeSimpleResponse> getMyChallenges(User user) {
        List<ChallengeMember> challengeMembers = challengeMemberRepository.findAllByUser(user);

        return challengeMembers.stream()
                .map(challengeMember -> {
                    Challenge challenge = challengeMember.getChallenge();

                    List<Stamp> stamps = stampRepository.findAllByChallenge(challenge);
                    int restaurantCount = stamps.size();
                    int completedCount = (int) stamps.stream()
                            .filter(stamp -> stamp.getRate() != null).count();
                    int memberCount = challengeMemberRepository.countByChallenge(challenge);

                    double progress = restaurantCount == 0 ? 0.0 : (completedCount * 100.0) / restaurantCount;
                    double roundedProgress = BigDecimal.valueOf(progress)
                            .setScale(1, RoundingMode.HALF_UP)
                            .doubleValue();

                    return ChallengeSimpleResponse.builder()
                            .challengeId(challenge.getId())
                            .title(challenge.getTitle())
                            .description(challenge.getDescription())
                            .createdAt(challenge.getCreatedAt())
                            .memberCount(memberCount)
                            .restaurantCount(restaurantCount)
                            .completedCount(completedCount)
                            .progress(roundedProgress)
                            .build();
                })
                .toList();
    }
}
