package me.minkyoung.buddy_back.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatMessageBroadcastResponseDto {
    //서버 -> 구독자에게 브로드캐스트
    private Long messageId;
    private Long roomId;
    private Long senderId;
    private String senderName;
    private String content;
    private LocalDateTime createdAt;
}
