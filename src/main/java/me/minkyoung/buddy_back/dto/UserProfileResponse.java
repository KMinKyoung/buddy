package me.minkyoung.buddy_back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileResponse {
    private Long userId;
    private String name;
    private boolean isMe;
    private boolean isFollowing; //내가 상대를 팔로우 중인지
    private boolean isFollower; //상대가 나를 팔로우 중인지
    private boolean isMutual; //맞팔 여부

    private int followerCount;
    private int followingCount;
    private int postCount;

    public static UserProfileResponse of(
            Long userId,
            String name,
            boolean isMe,
            boolean isFollowing,
            boolean isFollower,
            int followerCount,
            int followingCount,
            int postCount
    ){
        return UserProfileResponse.builder()
                .userId(userId)
                .name(name)
                .isMe(isMe)
                .isFollowing(isFollowing)
                .isFollower(isFollower)
                .isMutual(isFollowing && isFollower)
                .followerCount(followerCount)
                .followingCount(followingCount)
                .postCount(postCount)
                .build();
    }
}