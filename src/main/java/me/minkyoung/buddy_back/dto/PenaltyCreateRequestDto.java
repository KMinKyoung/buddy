package me.minkyoung.buddy_back.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.minkyoung.buddy_back.domain.PenaltyStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class PenaltyCreateRequestDto {
    //수동적으로 제재를 줄때 이용
    private Long userId;

    //어떤 제재인지(WARNING,LIMMITED,TEMP_BAN, PERM_BAN)
    private PenaltyStatus penaltyStatus;

    //기간이 존재하는 제재일 경우
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    //관리자가 확인을위한 메모
    private String reason;

    //이 제재의 근거가 되는 신고들
    private List<Long> reportIds;

}
