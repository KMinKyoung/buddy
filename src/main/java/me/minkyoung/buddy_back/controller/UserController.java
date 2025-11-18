package me.minkyoung.buddy_back.controller;

import lombok.RequiredArgsConstructor;
import me.minkyoung.buddy_back.dto.LogInRequestDto;
import me.minkyoung.buddy_back.dto.LogInResponseDto;
import me.minkyoung.buddy_back.dto.SignUpRequestDto;
import me.minkyoung.buddy_back.service.AuthService;
import me.minkyoung.buddy_back.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignUpRequestDto signUpRequestDto){
        userService.signup(signUpRequestDto);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<LogInResponseDto> login(@RequestBody LogInRequestDto  logInRequestDto){
        LogInResponseDto responseDto = authService.login(logInRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    //로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(Authentication authentication){
        authService.logout(authentication);
        return ResponseEntity.ok().build();
    }
}
