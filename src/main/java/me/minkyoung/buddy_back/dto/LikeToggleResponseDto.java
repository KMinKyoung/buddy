package me.minkyoung.buddy_back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class LikeToggleResponseDto {
    private boolean liked;
    private long likeCount;
}
