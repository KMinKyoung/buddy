package me.minkyoung.buddy_back.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ChatMessageListResponseDto {
    //전에 작성한 히스토리까지 조회
    private Long roomId;
    private List<ChatMessageBroadcastResponseDto> message;

}
