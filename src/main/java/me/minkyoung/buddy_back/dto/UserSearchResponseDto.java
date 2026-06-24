package me.minkyoung.buddy_back.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSearchResponseDto {
    private Long userId;
    private String name;
    //private String prifuleImageUrl 나중에 프로필 이미지 기능도 추가
}
