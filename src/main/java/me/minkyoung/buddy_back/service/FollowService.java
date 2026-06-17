package me.minkyoung.buddy_back.service;


import lombok.RequiredArgsConstructor;
import me.minkyoung.buddy_back.dto.FollowStatusResponse;
import me.minkyoung.buddy_back.dto.FollowUserResponse;
import me.minkyoung.buddy_back.entity.Follow;
import me.minkyoung.buddy_back.entity.User;
import me.minkyoung.buddy_back.repository.FollowRepository;
import me.minkyoung.buddy_back.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    // 팔로우 생성
    public FollowStatusResponse follow(Authentication authentication, Long targetUserId) {
        // 로그인 사용자 조회
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 팔로우 대상 사용자 조회
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 사용자입니다."));


        // 자기 자신 팔로우인지 확인
        if(user.getId().equals(targetUser.getId())) {
            throw new IllegalArgumentException("자기 자신은 팔로우 할 수 없습니다.");

        }

        // 이미 팔로우 중인지 확인
        boolean alreadyFollowing = followRepository.existsByFollowerIdAndFollowingId(
                user.getId(),
                targetUser.getId()
        );

        if(alreadyFollowing) {
            throw new IllegalArgumentException("이미 팔로우 중인 사용자입니다.");
        }

        // follow 생성(빌더)
        Follow follow = Follow.builder()
                .follower(user)
                .following(targetUser)
                .build();

        followRepository.save(follow);

        //followStatusResponse 반환
        return createFollowStatusResponse(user.getId(), targetUser.getId());

    }

    // 팔로우 삭제 - 언팔로우
    public FollowStatusResponse unfollow(Authentication authentication, Long targetUserId) {
        // 로그인 사용자 조회
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 언팔로우 대상 사용자 조회
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        //기존 팔로우 관계 조회
        Follow follow = followRepository.findByFollowerIdAndFollowingId(
                user.getId(),
                targetUser.getId()
        ).orElseThrow(()->new IllegalArgumentException("팔로우 관계가 존재하지 않습니다."));

        // 팔로우 관계 삭제
        followRepository.delete(follow);

        // 팔로우 상태 응답 반환
        return createFollowStatusResponse(user.getId(), targetUser.getId());
    }

    // 프로필에서 확인할 팔로우 상태 조회
    @Transactional(readOnly = true)
    public FollowStatusResponse getFollowStatus(Authentication authentication, Long targetUserId) {
        // 로그인 사용자 조회
        String email = authentication.getName();
        User user =userRepository.findByEmail(email)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 대상 사용자 존재 여부 확인
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 팔로우 상태 응답 반환
        return createFollowStatusResponse(user.getId(), targetUser.getId());
    }

    // 팔로잉 조회
    @Transactional(readOnly = true)
    public Page<FollowUserResponse> getFollowings(Authentication authentication, Long userId, Pageable pageable) {
        // 로그인 사용자 조회
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 조회 대상 사용자 존재 여부 확인
        User targetUser = userRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // targetUser가 팔로우하는 목록 조회
        Page<Follow> followings = followRepository.findByFollowerId(
                targetUser.getId(),
                pageable
        );

        // Follow -> FollowUserResponse 반환
        return followings.map(follow -> {
            User followingUser = follow.getFollowing();

            boolean isFollowing = followRepository.existsByFollowerIdAndFollowingId(
                    user.getId(),
                    followingUser.getId()
            );
            boolean isFollower = followRepository.existsByFollowerIdAndFollowingId(
                    followingUser.getId(),
                    user.getId()
            );

            return FollowUserResponse.builder()
                    .userId(followingUser.getId())
                    .nickname(followingUser.getName())
                    .isFollowing(isFollowing)
                    .isMutual(isFollowing && isFollower)
                    .build();
        });
    }

    // 팔로워 조회
    @Transactional(readOnly = true)
    public Page<FollowUserResponse> getFollowers(
            Authentication authentication,
            Long userId,
            Pageable pageable
    ) {
        // 로그인 사용자 조회
        String email = authentication.getName();
        User loginUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 조회 대상 사용자 존재 여부 확인
        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // targetUser를 팔로우하는 목록 조회
        Page<Follow> followers = followRepository.findByFollowingId(
                targetUser.getId(),
                pageable
        );

        // Follow -> FollowUserResponse 변환
        return followers.map(follow -> {
            User followerUser = follow.getFollower();

            boolean isFollowing = followRepository.existsByFollowerIdAndFollowingId(
                    loginUser.getId(),
                    followerUser.getId()
            );

            boolean isFollower = followRepository.existsByFollowerIdAndFollowingId(
                    followerUser.getId(),
                    loginUser.getId()
            );

            return FollowUserResponse.builder()
                    .userId(followerUser.getId())
                    .nickname(followerUser.getName())
                    .isFollowing(isFollowing)
                    .isMutual(isFollowing && isFollower)
                    .build();
        });
    }

    private FollowStatusResponse createFollowStatusResponse(Long currentUserId, Long targetUserId) {

        boolean isFollowing = followRepository.existsByFollowerIdAndFollowingId(
                currentUserId,
                targetUserId
        );

        boolean isFollower = followRepository.existsByFollowerIdAndFollowingId(
                targetUserId,
                currentUserId
        );

        int followerCount = followRepository.countByFollowingId(targetUserId);
        int followingCount = followRepository.countByFollowerId(targetUserId);

        return FollowStatusResponse.of(
                targetUserId,
                isFollowing,
                isFollower,
                followerCount,
                followingCount
        );
    }
}
