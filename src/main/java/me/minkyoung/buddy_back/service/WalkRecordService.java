package me.minkyoung.buddy_back.service;


import lombok.RequiredArgsConstructor;
import me.minkyoung.buddy_back.dto.WalkRecordSaveRequest;
import me.minkyoung.buddy_back.dto.WalkRoutePointRequest;
import me.minkyoung.buddy_back.dto.WalkTodayResponseDto;
import me.minkyoung.buddy_back.entity.User;
import me.minkyoung.buddy_back.entity.WalkRecord;
import me.minkyoung.buddy_back.entity.WalkRoutePoint;
import me.minkyoung.buddy_back.repository.UserRepository;
import me.minkyoung.buddy_back.repository.WalkRecordRepository;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WalkRecordService {

    private final WalkRecordRepository walkRecordRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long saveWalkRecord(WalkRecordSaveRequest request, Authentication authentication) {
        String email =  authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        WalkRecord walkRecord = WalkRecord.builder()
                .user(user)
                .stepCount(request.getStepCount())
                .distanceMeters(request.getDistanceMeters())
                .startedAt(request.getStartedAt())
                .endedAt(request.getEndedAt())
                .build();

        if (request.getRoutePoints() != null) {
            for (WalkRoutePointRequest walkRoutePoint : request.getRoutePoints()) {
                WalkRoutePoint point = WalkRoutePoint.builder()
                        .latitude(walkRoutePoint.getLatitude())
                        .longitude(walkRoutePoint.getLongitude())
                        .sequence(walkRoutePoint.getSequence())
                        .recordedAt(walkRoutePoint.getRecordedAt())
                        .build();

                walkRecord.addRoutePoint(point);
            }
        }

        WalkRecord saved = walkRecordRepository.save(walkRecord);

        return saved.getId();
    }

    @Transactional(readOnly = true)
    public WalkTodayResponseDto getTodaySteps(Authentication authentication) {
        String email =  authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 사용자입니다."));

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime startOfNextDay = today.plusDays(1).atStartOfDay();;

        int todaySteps = walkRecordRepository.sumTodaySteps(
                user.getId(),
                startOfDay,
                startOfDay
        );

        int goalSteps = 10000;

        return new WalkTodayResponseDto(todaySteps, goalSteps);
    }

}
