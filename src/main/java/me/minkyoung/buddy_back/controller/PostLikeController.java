package me.minkyoung.buddy_back.controller;

import lombok.RequiredArgsConstructor;
import me.minkyoung.buddy_back.dto.LikeToggleResponseDto;
import me.minkyoung.buddy_back.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posts/")
public class PostLikeController {
    private final LikeService likeService;

    @PostMapping("/{postId}/likes")
    public ResponseEntity<LikeToggleResponseDto> createLike(@PathVariable Long postId, Authentication authentication){
        LikeToggleResponseDto responseDto = likeService.postLike(authentication,postId);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{postId}/likes")
    public ResponseEntity<LikeToggleResponseDto> deleteLike(@PathVariable Long postId, Authentication authentication){
        LikeToggleResponseDto responseDto = likeService.cancelPostLike(authentication,postId);
        return ResponseEntity.ok(responseDto);
    }
}
