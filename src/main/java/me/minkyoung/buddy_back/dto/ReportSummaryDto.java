package me.minkyoung.buddy_back.dto;

import lombok.Builder;
import lombok.Getter;
import me.minkyoung.buddy_back.domain.ReportStatus;
import me.minkyoung.buddy_back.domain.ReportType;
import me.minkyoung.buddy_back.entity.Report;

@Getter
@Builder
public class ReportSummaryDto { //관리자 확인용으로 page로 확인하기 위한 간단한 데이터 응답용
    private Long id;
    private ReportType reportType;
    private ReportStatus reportStatus;

    public static ReportSummaryDto from(Report report){
        return ReportSummaryDto.builder()
                .id(report.getId())
                .reportType(report.getReportType())
                .reportStatus(report.getStatus())
                .build();
    }
}
