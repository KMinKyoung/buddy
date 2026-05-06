package me.minkyoung.buddy_back.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder //빌더 패턴
public class ResponsePostDto {

    //로그인, 회원가입 추가 시 작성자 id를 가져와서 인증,인가를 포함시켜야함
    private Long id;
    private Long user_id;
    private String name;
    private String title;
    private String description;
    private String image_url;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long likeCount;
    private Boolean likedByMe;
}
