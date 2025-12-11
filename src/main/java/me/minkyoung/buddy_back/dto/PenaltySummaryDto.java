package me.minkyoung.buddy_back.dto;

import lombok.Builder;
import lombok.Getter;
import me.minkyoung.buddy_back.domain.PenaltyStatus;
import me.minkyoung.buddy_back.entity.Penalty;

import java.time.LocalDateTime;

@Getter
@Builder
public class PenaltySummaryDto {
    //제재 목록
    private Long id;
    private Long userId;
    private String userEmail;
    private PenaltyStatus penaltyStatus;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private LocalDateTime createdAt;

    public static PenaltySummaryDto from(Penalty penalty){
        return PenaltySummaryDto.builder()
                .id(penalty.getId())
                .userId(penalty.getUser().getId())
                .userEmail(penalty.getUser().getEmail())
                .penaltyStatus(penalty.getPenaltyStatus())
                .startAt(penalty.getStartAt())
                .endAt(penalty.getEndAt())
                .createdAt(penalty.getCreatedAt())
                .build();
    }
}
