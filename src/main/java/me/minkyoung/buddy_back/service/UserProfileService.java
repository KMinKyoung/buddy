package me.minkyoung.buddy_back.service;

import lombok.RequiredArgsConstructor;
import me.minkyoung.buddy_back.dto.ResponsePostDto;
import me.minkyoung.buddy_back.dto.UserProfileResponse;
import me.minkyoung.buddy_back.entity.Post;
import me.minkyoung.buddy_back.entity.User;
import me.minkyoung.buddy_back.repository.FollowRepository;
import me.minkyoung.buddy_back.repository.PostRepository;
import me.minkyoung.buddy_back.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserProfileService {
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final PostRepository postRepository;

    public UserProfileResponse getUserProfile(Authentication authentication, Long targetUserId) {
        // 1. 현재 로그인 사용자 조회
        String email = authentication.getName();
        User loginUser = userRepository.findByEmail(email)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 2. 프로필 대상 사용자 조회
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Long loginUserId = loginUser.getId();
        Long profileUserId = targetUser.getId();

        // 3. 내 프로필인가
        boolean isMe = loginUserId.equals(profileUserId);

        // 4. 팔로우 관계 조회
        boolean isFollowing = followRepository.existsByFollowerIdAndFollowingId(loginUserId, profileUserId);

        boolean isFollower = followRepository.existsByFollowerIdAndFollowingId(loginUserId, profileUserId);

        // 5. 팔로워 / 팔로잉 수 조회
        int followerCount = followRepository.countByFollowerId(profileUserId);
        int followingCount = followRepository.countByFollowingId(profileUserId);
        int postCount = postRepository.countByUserId(profileUserId);

        return UserProfileResponse.of(
                profileUserId,
                targetUser.getName(),
                isMe,
                isFollowing,
                isFollower,
                followerCount,
                followingCount,
                postCount
        );


    }

    public Page<ResponsePostDto> getUserPosts(
            Authentication authentication,
            Long userId,
            Pageable pageable
    ) {
        // 1. 로그인 사용자 조회
        String email = authentication.getName();
        User loginUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 2. 프로필 대상 사용자 존재 여부 확인
        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 3. 해당 사용자가 작성한 글 조회
        Page<Post> posts = postRepository.findByUserIdOrderByCreatedAtDesc(
                targetUser.getId(),
                pageable
        );

        // 4. Post -> ResponsePostDto 변환
        return posts.map(post -> ResponsePostDto.builder()
                .id(post.getId())
                .user_id(post.getUser().getId())
                .name(post.getUser().getName())
                .title(post.getTitle())
                .description(post.getDescription())
                .image_url(post.getImgUrl())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build());
    }
}
