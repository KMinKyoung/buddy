package me.minkyoung.buddy_back.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.minkyoung.buddy_back.domain.ReportType;

@Getter
@NoArgsConstructor
public class ReportRequestDto { //사용자가 신고하기 위해 넣는 데이터, "신고가 접수되었습니다."만 반환받게 된다.
    private Long reportedUserId;
    private ReportType reportType;
    private String reason;

}
