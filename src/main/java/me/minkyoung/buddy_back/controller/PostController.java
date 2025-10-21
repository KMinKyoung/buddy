package me.minkyoung.buddy_back.controller;

import lombok.RequiredArgsConstructor;
import me.minkyoung.buddy_back.dto.RequestPostDto;
import me.minkyoung.buddy_back.dto.ResponsePostDto;
import me.minkyoung.buddy_back.entity.Post;
import me.minkyoung.buddy_back.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController //Rest API 요청 컨트롤러
@RequestMapping("/api/posts") //URL 매핑
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("")
    public ResponseEntity createPost(@RequestBody RequestPostDto requestPostDto) {
        ResponsePostDto responsePostDto = postService.createPost(requestPostDto);
        return ResponseEntity.ok().body(responsePostDto);
    }

}
