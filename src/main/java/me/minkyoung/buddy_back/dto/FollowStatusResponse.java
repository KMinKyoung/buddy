package me.minkyoung.buddy_back.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FollowStatusResponse {
    private Long targetUserId; //팔로우 상태를 확인할 대상 사용자
    private boolean isFollowing;
    private boolean isFollower;
    private boolean isMutual;
    private int followerCount;
    private int followingCount;

    public static FollowStatusResponse of(
            Long targetUserId,
            boolean isFollowing,
            boolean isFollower,
            int followerCount,
            int followingCount
    ){
        return FollowStatusResponse.builder()
                .targetUserId(targetUserId)
                .isFollowing(isFollowing)
                .isFollower(isFollower)
                .isMutual(isFollowing && isFollower)
                .followerCount(followerCount)
                .followingCount(followingCount)
                .build();
    }
}
