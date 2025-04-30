package com.matzzangteam.matzzang.service;

import com.matzzangteam.matzzang.dto.friendship.FriendRequest;
import com.matzzangteam.matzzang.dto.friendship.FriendListResponse;
import com.matzzangteam.matzzang.entity.Friendship;
import com.matzzangteam.matzzang.entity.User;
import com.matzzangteam.matzzang.exception.ClientErrorException;
import com.matzzangteam.matzzang.repository.FriendshipRepository;
import com.matzzangteam.matzzang.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendshipService {

    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;

    @Transactional
    public void sendFriendRequest(User fromUser, FriendRequest request) {
        User toUser = userRepository.findByInviteCode(request.inviteCode())
                .orElseThrow(() -> new ClientErrorException(HttpStatus.NOT_FOUND, "User with this invite code not exist"));

        if (fromUser.getId().equals(toUser.getId())) {
            throw new ClientErrorException(HttpStatus.BAD_REQUEST, "Cannot send friend request to yourself");
        }

        boolean exists = friendshipRepository.findFriendshipBetweenUsers(fromUser, toUser).isPresent();
        if (exists) {
            throw new ClientErrorException(HttpStatus.BAD_REQUEST, "Request already sent");
        }

        Friendship friend = Friendship.builder()
                .fromUser(fromUser)
                .toUser(toUser)
                .status(Friendship.FriendshipStatus.PENDING)
                .build();

        friendshipRepository.save(friend);
    }

    public List<FriendListResponse> getReceivedRequests(User toUser) {
        List<Friendship> pendingRequests = friendshipRepository.findByToUserAndStatus(toUser, Friendship.FriendshipStatus.PENDING);

        return pendingRequests.stream()
                .map(friendship -> new FriendListResponse(
                        friendship.getFromUser().getId(),
                        friendship.getFromUser().getNickname(),
                        friendship.getFromUser().getProfileImageUrl()
                ))
                .toList();
    }

    @Transactional
    public void acceptFriendRequest(User toUser, Long fromUserId) {
        Friendship friendship = friendshipRepository
                .findByFromUserIdAndToUserIdAndStatus(fromUserId, toUser.getId(), Friendship.FriendshipStatus.PENDING)
                .orElseThrow(() -> new ClientErrorException(HttpStatus.NOT_FOUND, "Friend request not found"));

        friendship.setStatus(Friendship.FriendshipStatus.ACCEPTED);
        friendshipRepository.save(friendship);
    }

    @Transactional(readOnly = true)
    public List<FriendListResponse> getFriends(User user) {
        List<Friendship> friendships = friendshipRepository
                .findAllByUserAndStatus(user, Friendship.FriendshipStatus.ACCEPTED);

        return friendships.stream()
                .map(friendship -> {
                    User friend = friendship.getFromUser().getId().equals(user.getId())
                            ? friendship.getToUser()
                            : friendship.getFromUser();
                    return new FriendListResponse(
                            friend.getId(),
                            friend.getNickname(),
                            friend.getProfileImageUrl()
                    );
                })
                .toList();
    }
}
