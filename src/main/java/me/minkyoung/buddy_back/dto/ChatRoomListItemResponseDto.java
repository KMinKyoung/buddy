package me.minkyoung.buddy_back.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatRoomListItemResponseDto {
    //방 목록(최근 메시지/시간 포함)
    private Long roomId;
    private String type;
    private String name;
    private String lastMessage;
    private LocalDateTime lastMessageAt;

}
