package me.minkyoung.buddy_back.controller;

import lombok.RequiredArgsConstructor;
import me.minkyoung.buddy_back.dto.RequestPostDto;
import me.minkyoung.buddy_back.dto.ResponsePostDto;
import me.minkyoung.buddy_back.entity.Post;
import me.minkyoung.buddy_back.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{postId}")
    public ResponseEntity<ResponsePostDto> findById(@PathVariable Long postId) {
        ResponsePostDto responsePostDto = postService.getbyIdPost(postId);
        return ResponseEntity.ok().body(responsePostDto);
    }

    //10개에 한페이지, 생성일 + 오름차순으로 정렬
    @GetMapping("")
    public ResponseEntity<Page<ResponsePostDto>> findAll(@PageableDefault(
            size = 10,
            sort = "createdAt",
            direction = Sort.Direction.ASC
    ) Pageable pageable) {
        Page<ResponsePostDto> responsePostDto = postService.getByAllPost(pageable);
        return ResponseEntity.ok(responsePostDto);
    }
}
