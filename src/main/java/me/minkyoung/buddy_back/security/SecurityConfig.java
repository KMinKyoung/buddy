package me.minkyoung.buddy_back.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // ✅ CSRF 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/posts/**").permitAll() // ✅ 게시글 API는 인증 없이 허용
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}
