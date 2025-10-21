package me.minkyoung.buddy_back.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RequestPostDto {
    //타이틀, 내용,이미지
    private String title;
    private String description;
    private String image_url;
}
