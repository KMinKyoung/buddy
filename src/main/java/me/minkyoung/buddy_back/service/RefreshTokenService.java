package me.minkyoung.buddy_back.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.minkyoung.buddy_back.domain.RefreshToken;
import me.minkyoung.buddy_back.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository  refreshTokenRepository;

    //로그인 시 사용자별 리프레시 토큰 저장 및 업데이트
    @Transactional
    public void save(Long userId, String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByUserId(userId)
                .map(entity ->entity.update(refreshToken))
                .orElseGet(() -> new RefreshToken(userId, refreshToken));

        refreshTokenRepository.save(token);
    }

    public RefreshToken findByRefreshToken(String refreshToken){
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(()->new IllegalArgumentException("토큰을 찾을 수 없습니다."));
    }

    //로그아웃, 리프래시 토큰 삭제
    @Transactional
    public void deleteByUserId(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
}
