package me.minkyoung.buddy_back.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.minkyoung.buddy_back.domain.Role;
import me.minkyoung.buddy_back.dto.SignUpRequestDto;
import me.minkyoung.buddy_back.dto.UserSearchResponseDto;
import me.minkyoung.buddy_back.entity.User;
import me.minkyoung.buddy_back.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //회원가입
    public Long signup(SignUpRequestDto signUpRequestDto){

        if(userRepository.existsByEmail(signUpRequestDto.getEmail())){
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        User user = User.builder()
                .email(signUpRequestDto.getEmail())
                .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                .name(signUpRequestDto.getName())
                .role(Role.USER)
                .build();

        return userRepository.save(user).getId();
    }


    public User findById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 유저입니다."));
    }

    //이름으로 유저 찾기
    public List<UserSearchResponseDto> searchUsers(Authentication authentication, String keyword){

        String email = authentication.getName();

        User loginUser = userRepository.findByEmail(email)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if(keyword == null || keyword.trim().isEmpty()){
            return List.of();
        }

        String trimmedKeyword = keyword.trim();

        Pageable pageable = PageRequest.of(0,20);

        return userRepository.searchByName(trimmedKeyword,loginUser.getId(),pageable)
                .stream()
                .map(user->UserSearchResponseDto.builder()
                        .userId(user.getId())
                        .name(user.getName())
                        .build())
                .toList();
    }

}
