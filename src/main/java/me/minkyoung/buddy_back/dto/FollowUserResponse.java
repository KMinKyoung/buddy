package me.minkyoung.buddy_back.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowUserResponse {
    private Long userId;
    private String nickㅜame;
    private boolean isFollowing;
    private boolean isMutual;
}
