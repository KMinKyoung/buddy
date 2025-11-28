package me.minkyoung.buddy_back.repository;

import me.minkyoung.buddy_back.entity.Penalty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PenaltyRepository extends JpaRepository<Penalty, Long> {
}
