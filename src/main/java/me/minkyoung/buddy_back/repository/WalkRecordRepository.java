package me.minkyoung.buddy_back.repository;

import me.minkyoung.buddy_back.entity.WalkRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface WalkRecordRepository extends JpaRepository<WalkRecord, Long> {

    @Query("""
            select coalesce(sum(w.stepCount), 0)
            from WalkRecord w
            where w.user.id = :userId
              and w.startedAt >= :startOfDay
              and w.startedAt < :startOfNextDay
            """)
    int sumTodaySteps(
            @Param("userId") Long userId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("startOfNextDay") LocalDateTime startOfNextDay
            );
}
