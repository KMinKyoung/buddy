package me.minkyoung.buddy_back.controller;

import lombok.RequiredArgsConstructor;
import me.minkyoung.buddy_back.dto.*;
import me.minkyoung.buddy_back.service.AuthService;
import me.minkyoung.buddy_back.service.MyPageService;
import me.minkyoung.buddy_back.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthService authService;
    private final MyPageService myPageService;

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

    //내가 작성한 글 가져오기
    @GetMapping(value = "/posts", produces = "application/json")
    public ResponseEntity<Page<MyPostResponseDto>> myPosts(Pageable pageable, Authentication authentication){
        Page<MyPostResponseDto>result = myPageService.getById(pageable,authentication);
        return  ResponseEntity.ok(result);
    }

    //내가 작성한 댓글 가져오기
    @GetMapping(value = "/comments", produces = "application/json")
    public ResponseEntity<Page<MyCommentResponseDto>> myComments(Pageable pageable,Authentication authentication){
        Page<MyCommentResponseDto> result = myPageService.getCommentById(pageable, authentication);
        return  ResponseEntity.ok(result);
    }

    //내가 좋아요한 글 가져오기
}
