package me.minkyoung.buddy_back.dto;

import lombok.Builder;
import lombok.Getter;
import me.minkyoung.buddy_back.domain.PenaltyStatus;
import me.minkyoung.buddy_back.entity.Penalty;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PenaltyDetailDto {
    private Long id;
    private Long userId;
    private String userEmail;
    private PenaltyStatus penaltyStatus;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private LocalDateTime createdAt;
    private String reason;
    private List<ReportSummaryDto> reports;

    public static PenaltyDetailDto from(Penalty penalty, List<ReportSummaryDto> reports) {
        return PenaltyDetailDto.builder()
                .id(penalty.getId())
                .userId(penalty.getUser().getId())
                .userEmail(penalty.getUser().getEmail())
                .penaltyStatus(penalty.getPenaltyStatus())
                .startAt(penalty.getStartAt())
                .endAt(penalty.getEndAt())
                .createdAt(penalty.getCreatedAt())
                .reason(penalty.getReason())
                .reports(reports)
                .build();
    }
}
