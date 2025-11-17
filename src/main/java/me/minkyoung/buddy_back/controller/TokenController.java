package me.minkyoung.buddy_back.controller;

import lombok.RequiredArgsConstructor;
import me.minkyoung.buddy_back.dto.CreateAccessTokenRequest;
import me.minkyoung.buddy_back.dto.CreateAccessTokenResponse;
import me.minkyoung.buddy_back.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TokenController {
    private final TokenService tokenService;

    @PostMapping("/api/token")
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(@RequestBody CreateAccessTokenRequest createAccessTokenRequest) {
        String newAccessToken = tokenService.createNewAccessToken(createAccessTokenRequest.getRefreshToken());

        return ResponseEntity.ok(new CreateAccessTokenResponse(newAccessToken));
    }
}
