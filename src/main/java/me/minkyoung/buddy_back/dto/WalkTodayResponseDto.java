package me.minkyoung.buddy_back.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WalkTodayResponseDto {
    private int todaySteps;
    private int goalSteps;
}
