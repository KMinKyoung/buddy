package me.minkyoung.buddy_back.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatMessageSendRequestDto {
    //클라이언트 -> 서버, 전송
    @NotBlank
    @Size(max=2000)
    private String content;
    private String clientMessageId;
}
