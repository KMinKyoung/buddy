package me.minkyoung.buddy_back.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentRequest {
    private int post_id;
    private String description;
}
