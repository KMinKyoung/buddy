package me.minkyoung.buddy_back.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponse {
    private Long id;
    private Long post_id;
    private Long user_id;
    private String description;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
