package me.minkyoung.buddy_back.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.minkyoung.buddy_back.dto.GoogleLoginRequest;
import me.minkyoung.buddy_back.dto.TokenResponse;
import me.minkyoung.buddy_back.service.GoogleAutheService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final GoogleAutheService googleAutheService;

    @PostMapping("/google")
    public ResponseEntity<TokenResponse> googleLogin(@Valid @RequestBody GoogleLoginRequest request) {
        System.out.println("구글 로그인 진입");
        TokenResponse response = googleAutheService.loginWithGoogle(request.getIdToken());
        return ResponseEntity.ok(response);
    }
}
