package me.minkyoung.buddy_back.service;

import lombok.RequiredArgsConstructor;
import me.minkyoung.buddy_back.config.jwt.TokenProvider;
import me.minkyoung.buddy_back.dto.LogInRequestDto;
import me.minkyoung.buddy_back.dto.LogInResponseDto;
import me.minkyoung.buddy_back.entity.User;
import me.minkyoung.buddy_back.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    //JWT기반 로그인
    public LogInResponseDto login(LogInRequestDto requestDto){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 사용자입니다."));

        //토큰 생성
        String accessToken = tokenProvider.generateToken(user, Duration.ofHours(3));
        String refreshToken = tokenProvider.generateToken(user, Duration.ofDays(7));

        refreshTokenService.save(user.getId(),refreshToken);

        return new LogInResponseDto(accessToken,refreshToken);
    }

    //로그아웃
    public void logout(Authentication authentication){
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        //유저의 refreshtoken 삭제
        refreshTokenService.deleteByUserId(user.getId());
    }
}
