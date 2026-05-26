package me.minkyoung.buddy_back.controller;

import lombok.RequiredArgsConstructor;
import me.minkyoung.buddy_back.dto.WalkRecordSaveRequest;
import me.minkyoung.buddy_back.dto.WalkTodayResponseDto;
import me.minkyoung.buddy_back.service.WalkRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class WalkRecordController {
    private final WalkRecordService walkRecordService;

    @PostMapping("/walk-records")
    public ResponseEntity<?> saveWalkRecord(@RequestBody WalkRecordSaveRequest  request, Authentication authentication) {

        Long walkRecordId = walkRecordService.saveWalkRecord(request, authentication);

        return ResponseEntity.ok(Map.of(
                "message", "걷기 기록 저장 성공",
                "walkRecordId", walkRecordId
        ));
    }

    @GetMapping("/walk-records/today")
    public ResponseEntity<WalkTodayResponseDto> getTodaySteps(Authentication authentication) {
        WalkTodayResponseDto responseDto = walkRecordService.getTodaySteps(authentication);
        return ResponseEntity.ok(responseDto);
    }
}
