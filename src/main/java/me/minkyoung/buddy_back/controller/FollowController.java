package me.minkyoung.buddy_back.controller;

import lombok.RequiredArgsConstructor;
import me.minkyoung.buddy_back.dto.FollowStatusResponse;
import me.minkyoung.buddy_back.dto.FollowUserResponse;
import me.minkyoung.buddy_back.service.FollowService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follows")
public class FollowController {
    private final FollowService followService;

    // 팔로우 생성
    @PostMapping("/{targetUserId}")
    public ResponseEntity<FollowStatusResponse> follow(
            Authentication authentication,
            @PathVariable Long targetUserId
    ) {
        FollowStatusResponse response = followService.follow(authentication, targetUserId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // 팔로우 삭제 - 언팔로우
    @DeleteMapping("/{targetUserId}")
    public ResponseEntity<FollowStatusResponse> unfollow(
            Authentication authentication,
            @PathVariable Long targetUserId
    ) {
        FollowStatusResponse response = followService.unfollow(authentication, targetUserId);

        return ResponseEntity.ok(response);
    }

    // 프로필에서 확인할 팔로우 상태 조회
    @GetMapping("/status/{targetUserId}")
    public ResponseEntity<FollowStatusResponse> getFollowStatus(
            Authentication authentication,
            @PathVariable Long targetUserId
    ) {
        FollowStatusResponse response = followService.getFollowStatus(authentication, targetUserId);

        return ResponseEntity.ok(response);
    }

    // 팔로잉 목록 조회
    @GetMapping("/{userId}/followings")
    public ResponseEntity<Page<FollowUserResponse>> getFollowings(
            Authentication authentication,
            @PathVariable Long userId,
            Pageable pageable
    ) {
        Page<FollowUserResponse> response = followService.getFollowings(
                authentication,
                userId,
                pageable
        );

        return ResponseEntity.ok(response);
    }

    // 팔로워 목록 조회
    @GetMapping("/{userId}/followers")
    public ResponseEntity<Page<FollowUserResponse>> getFollowers(
            Authentication authentication,
            @PathVariable Long userId,
            Pageable pageable
    ) {
        Page<FollowUserResponse> response = followService.getFollowers(
                authentication,
                userId,
                pageable
        );

        return ResponseEntity.ok(response);
    }
}
