package me.minkyoung.buddy_back.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
@NoArgsConstructor
public class CreateRoomDirectRequestDto {
    // 1대1 방 생성 요청
    @NotNull
    private Long targetUserId;
}
