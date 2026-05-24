package me.minkyoung.buddy_back.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class WalkRoutePointRequest {
    private double latitude;
    private double longitude;
    private int sequence;
    private LocalDateTime recordedAt;

}
