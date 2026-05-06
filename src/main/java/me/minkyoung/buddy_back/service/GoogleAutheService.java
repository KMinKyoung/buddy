package me.minkyoung.buddy_back.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.minkyoung.buddy_back.config.jwt.TokenProvider;
import me.minkyoung.buddy_back.domain.Role;
import me.minkyoung.buddy_back.dto.TokenResponse;
import me.minkyoung.buddy_back.entity.User;
import me.minkyoung.buddy_back.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class GoogleAutheService {
    private final GoogleTokenVerifier googleTokenVerifier;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    public TokenResponse loginWithGoogle(String idToken){
        GoogleIdToken.Payload payload = googleTokenVerifier.verify(idToken);

        String providerId = payload.getSubject(); //Google 고유 식별값
        String email = payload.getEmail();
        String name = (String) payload.get("name");

        User user = userRepository.findByProviderAndProviderId("GOOGLE",providerId)
                .orElseGet(()-> registerGoogleUser(email, name, providerId));

        String accessToken = tokenProvider.generateToken(user, Duration.ofHours(2));

        return new TokenResponse(accessToken);
    }

    private User registerGoogleUser(String email, String name, String providerId){
        User user = User.builder()
                .email(email)
                .name(name != null ? name : "Google User")
                .password(UUID.randomUUID().toString()) //일반 로그인 비밀번호와 분리된 임의의 값
                .role(Role.USER)
                .provider("GOOGLE")
                .providerId(providerId)
                .build();

        return userRepository.save(user);
    }
}
