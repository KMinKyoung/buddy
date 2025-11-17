package me.minkyoung.buddy_back.service;

import lombok.RequiredArgsConstructor;
import me.minkyoung.buddy_back.domain.RefreshToken;
import me.minkyoung.buddy_back.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository  refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken){
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(()->new IllegalArgumentException("토큰을 찾을 수 없습니다."));
    }
}
