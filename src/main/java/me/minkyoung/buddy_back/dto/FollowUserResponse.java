package me.minkyoung.buddy_back.dto;

import lombok.*;
import me.minkyoung.buddy_back.entity.User;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowUserResponse {
    private Long userId;
    private String nickname;
    private boolean isFollowing;
    private boolean isMutual;

    public static FollowUserResponse of(
            User user,
            boolean isFollowing,
            boolean isFollower
    ){
        return  FollowUserResponse.builder()
                .userId(user.getId())
                .nickname(user.getName())
                .isFollowing(isFollowing)
                .isMutual(isFollowing && isFollower)
                .build();
    }
}
