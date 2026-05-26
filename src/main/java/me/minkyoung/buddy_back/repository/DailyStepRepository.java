package me.minkyoung.buddy_back.repository;

import me.minkyoung.buddy_back.entity.DailyStep;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyStepRepository extends JpaRepository<DailyStep, Long> {

    Optional<DailyStep> findByUserIdAndStepDate(Long userId, LocalDate stepDate);
}
