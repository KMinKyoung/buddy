package me.minkyoung.buddy_back.repository;

import me.minkyoung.buddy_back.domain.PenaltyStatus;
import me.minkyoung.buddy_back.entity.Penalty;
import me.minkyoung.buddy_back.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PenaltyRepository extends JpaRepository<Penalty, Long> {
    //유저+상태별 누적 제재 수(경고 N번, 기능제한 N번 등)
    long countByUserAndPenaltyStatus(User user, PenaltyStatus penaltyStatus);

    //유저의 제재 이력 중 가장 최근 것(현재 상태 판단용도)
    Optional<Penalty> findToByUserOrderByCreatedAtDesc();
}
