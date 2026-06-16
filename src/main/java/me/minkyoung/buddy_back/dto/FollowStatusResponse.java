package me.minkyoung.buddy_back.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FollowStatusResponse {
    private Long targetUserid; //팔로우 상태를 확인할 대상 사용자
    private boolean isFollowing;
    private boolean isFollower;
    private boolean isMutual;
    private int followerCount;
    private int followingCount;
}
