package me.minkyoung.buddy_back.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CreateGroupRoomRequestDto {
    //그룹방 생성 요청
    @NotBlank
    @Size(max = 50)
    private String name; //방 이름
    private List<Long> memberIds;
}
