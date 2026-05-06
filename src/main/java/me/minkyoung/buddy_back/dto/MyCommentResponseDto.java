package me.minkyoung.buddy_back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class MyCommentResponseDto {
    private Long id;
    private Long post_id;
    private Long user_id;
    private String name;
    private String description;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private Long likeCount;
    private Boolean likedByMe;
}
