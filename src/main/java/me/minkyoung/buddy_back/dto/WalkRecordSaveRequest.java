package me.minkyoung.buddy_back.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class WalkRecordSaveRequest {
    private int stepCount;
    private Double distanceMeters;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private List<WalkRoutePointRequest> routePoints;

}
