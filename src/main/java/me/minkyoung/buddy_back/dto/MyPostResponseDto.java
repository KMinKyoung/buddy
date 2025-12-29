package me.minkyoung.buddy_back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class MyPostResponseDto {
    //작성한 postId, 작성한 UserId, UserName , 상세내용, 날짜, 이미지
    private Long id;
    private Long user_id;
    private String name;
    private String title;
    private String description;
    private String image_url;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
