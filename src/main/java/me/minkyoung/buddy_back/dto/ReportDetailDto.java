package me.minkyoung.buddy_back.dto;

import lombok.Builder;
import lombok.Getter;
import me.minkyoung.buddy_back.domain.ReportStatus;
import me.minkyoung.buddy_back.domain.ReportType;

@Getter
@Builder
public class ReportDetailDto { //관리자 확인용으로 자세한 신고내용을 확인하기 위한 내역들
    private Long id;
    private Long penaltyId; //신고로 연결된 제재
    //신고자
    private Long reporterId;
    private String reporterEmail;
    //신고 대상자
    private Long reportedUserId;
    private String reportedUserEmail;
    private ReportType reportType;
    private String reason;
    private ReportStatus reportStatus;
}
