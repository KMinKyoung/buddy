package me.minkyoung.buddy_back.config;

import lombok.RequiredArgsConstructor;
import me.minkyoung.buddy_back.config.jwt.TokenAuthenticationFilter;
import me.minkyoung.buddy_back.config.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())//  CSRF 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/oauth/**","/user/signup", "/user/login").permitAll() //로그인/회원가입, ㅐ며소 emd gjdyd
                        .requestMatchers(HttpMethod.GET,"/api/posts/**","/api/posts/{postId}/comments").permitAll()
                        .requestMatchers("/oci/**", "/api/oci/**").permitAll()// 글 조회는 인증 없이 허용
                        .requestMatchers(HttpMethod.POST, "/api/posts/*/comments").authenticated() //이후로는 인증이 있어야지..명시적으로 수정
                        .requestMatchers(HttpMethod.PUT, "/api/posts/*/comments/*").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/posts/*/comments/*").authenticated()
                        .requestMatchers(HttpMethod.GET, "/user/**").hasRole("USER")
                        .requestMatchers("/ws-stomp/**").permitAll()
                        .requestMatchers("/api/chat/**").authenticated()
                        .anyRequest().authenticated()
                )
                //매 요청마다 JWT 토큰 검사
                .addFilterBefore(new TokenAuthenticationFilter(tokenProvider),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/api/oci/**", "/oci/**");
    }

}
