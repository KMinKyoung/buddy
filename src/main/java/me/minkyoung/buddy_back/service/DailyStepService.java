package me.minkyoung.buddy_back.service;

import lombok.RequiredArgsConstructor;
import me.minkyoung.buddy_back.dto.StepUpdateRequestDto;
import me.minkyoung.buddy_back.dto.TodayStepResponseDto;
import me.minkyoung.buddy_back.entity.DailyStep;
import me.minkyoung.buddy_back.entity.User;
import me.minkyoung.buddy_back.repository.DailyStepRepository;
import me.minkyoung.buddy_back.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DailyStepService {
    private final DailyStepRepository dailyStepRepository;
    private final UserRepository userRepository;

    @Transactional
    public TodayStepResponseDto updateTodaySteps(StepUpdateRequestDto stepUpdateRequestDto, Authentication authentication) {
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 사용자입니다."));

        LocalDate today = LocalDate.now();

        DailyStep dailyStep = dailyStepRepository
                .findByUserIdAndStepDate(user.getId(), today)
                .orElseGet(()-> DailyStep.create(user, today,0));

        dailyStep.updateStepCount(stepUpdateRequestDto.getStepCount());

        DailyStep saved = dailyStepRepository.save(dailyStep);

        return new TodayStepResponseDto(
                saved.getStepCount(),
                saved.getGoalSteps()
        );
    }

    @Transactional(readOnly = true)
    public TodayStepResponseDto getTodaySteps(Authentication authentication) {
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        LocalDate today = LocalDate.now();

        return dailyStepRepository.findByUserIdAndStepDate(user.getId(), today)
                .map(dailyStep -> new TodayStepResponseDto(
                        dailyStep.getStepCount(),
                        dailyStep.getGoalSteps()
                ))
                .orElseGet(()-> new TodayStepResponseDto(0,10000));
    }
}
