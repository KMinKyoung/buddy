package me.minkyoung.buddy_back.controller;

import lombok.RequiredArgsConstructor;
import me.minkyoung.buddy_back.dto.StepUpdateRequestDto;
import me.minkyoung.buddy_back.dto.TodayStepResponseDto;
import me.minkyoung.buddy_back.service.DailyStepService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/steps")
public class DailyStepController {
    private final DailyStepService dailyStepService;

    @GetMapping("/today")
    public ResponseEntity<TodayStepResponseDto> getTodaySteps(Authentication authentication) {
        TodayStepResponseDto responseDto = dailyStepService.getTodaySteps(authentication);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/today")
    public ResponseEntity<TodayStepResponseDto> updateTodaySteps(@RequestBody StepUpdateRequestDto requestDto, Authentication authentication) {
        TodayStepResponseDto responseDto = dailyStepService.updateTodaySteps(requestDto, authentication);
        return ResponseEntity.ok(responseDto);
    }
}
