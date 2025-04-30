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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

        if (request.visitedAt() != null) {
            stamp.setVisitedAt(request.visitedAt());
        }
        if (request.memo() != null) {
            stamp.setMemo(request.memo());
        }
        if (request.rate() != null) {
            stamp.setRate(request.rate());
        }
        if (request.imageUrl() != null) {
            stamp.setImageUrl(request.imageUrl());
        }
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

    @Transactional
    public void updateChallenge(User user, Long challengeId, ChallengeModifyRequest request) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new ClientErrorException(HttpStatus.NOT_FOUND, "Challenge not found"));

        challenge.setTitle(request.title());
        challenge.setDescription(request.description());

        challengeMemberRepository.deleteAllByChallenge(challenge);

        List<Long> memberIds = new ArrayList<>(request.memberIds());
        memberIds.add(user.getId());

        List<User> members = userRepository.findAllById(memberIds);
        List<ChallengeMember> challengeMembers = members.stream()
                .map(member -> ChallengeMember.builder()
                        .challenge(challenge)
                        .user(member)
                        .build())
                .toList();

        challengeMemberRepository.saveAll(challengeMembers);

        //stamp
        List<Stamp> existingStamps = stampRepository.findAllByChallenge(challenge);
        Set<Long> requestRestaurantIds = new HashSet<>(request.restaurantIds());

        List<Stamp> stampsToDelete = existingStamps.stream()
                .filter(stamp -> !requestRestaurantIds.contains(stamp.getRestaurant().getId()))
                .toList();

        stampRepository.deleteAll(stampsToDelete);

        Set<Long> existingRestaurantIds = existingStamps.stream()
                .map(stamp -> stamp.getRestaurant().getId())
                .collect(Collectors.toSet());

        List<Long> newRestaurantIds = request.restaurantIds().stream()
                .filter(id -> !existingRestaurantIds.contains(id))
                .toList();

        List<Restaurant> newRestaurants = restaurantRepository.findAllById(newRestaurantIds);

        List<Stamp> newStamps = newRestaurants.stream()
                .map(restaurant -> Stamp.builder()
                        .challenge(challenge)
                        .restaurant(restaurant)
                        .visitedAt(null)
                        .memo(null)
                        .rate(null)
                        .imageUrl(null)
                        .build())
                .toList();

        stampRepository.saveAll(newStamps);
    }

    @Transactional
    public void deleteStamp(User user, Long stampId) {
        Stamp stamp = stampRepository.findById(stampId)
                .orElseThrow(() -> new ClientErrorException(HttpStatus.NOT_FOUND, "Stamp not found"));

        boolean isMember = challengeMemberRepository.existsByChallengeAndUser(stamp.getChallenge(), user);

        if (!isMember) {
            throw new ClientErrorException(HttpStatus.FORBIDDEN, "No permission to delete this stamp");
        }

        stampRepository.delete(stamp);
    }

    @Transactional
    public void deleteChallenge(User user, Long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new ClientErrorException(HttpStatus.NOT_FOUND, "Challenge not found"));

        boolean isMember = challengeMemberRepository.existsByChallengeAndUser(challenge, user);

        if (!isMember) {
            throw new ClientErrorException(HttpStatus.FORBIDDEN, "No permission to delete this challenge");
        }

        stampRepository.deleteAllByChallenge(challenge);

        challengeMemberRepository.deleteAllByChallenge(challenge);

        challengeRepository.delete(challenge);
    }
}
