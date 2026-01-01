package me.minkyoung.buddy_back.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatRoomResponseDto {
    //방 응답
    private Long roomId;
    private String type; //direct/group
    private String name;
    private LocalDateTime createdAt;
}
