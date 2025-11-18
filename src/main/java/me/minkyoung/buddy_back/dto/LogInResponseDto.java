package me.minkyoung.buddy_back.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LogInResponseDto {
    private String accessToken;
    private String refreshToken;
}
