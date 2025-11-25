package me.minkyoung.buddy_back.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponse {
    private int id;
    private int post_id;
    private int user_name;
    private String description;
    private LocalDateTime created_at;
}
