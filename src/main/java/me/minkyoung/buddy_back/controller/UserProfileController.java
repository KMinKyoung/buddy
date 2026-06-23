package me.minkyoung.buddy_back.controller;

import lombok.RequiredArgsConstructor;
import me.minkyoung.buddy_back.dto.ResponsePostDto;
import me.minkyoung.buddy_back.dto.UserProfileResponse;
import me.minkyoung.buddy_back.service.UserProfileService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/{userId}/profile")
    public ResponseEntity<UserProfileResponse> getUserProfile(Authentication authentication, @PathVariable Long userId) {
        UserProfileResponse response = userProfileService.getUserProfile(authentication,userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/posts")
    public ResponseEntity<Page<ResponsePostDto>> getUserPosts(            Authentication authentication, @PathVariable Long userId, Pageable pageable) {
        Page<ResponsePostDto> response = userProfileService.getUserPosts(
                authentication,
                userId,
                pageable
        );
        return ResponseEntity.ok(response);
    }
}
