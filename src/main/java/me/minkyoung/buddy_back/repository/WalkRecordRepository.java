package me.minkyoung.buddy_back.repository;

import me.minkyoung.buddy_back.entity.WalkRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalkRecordRepository extends JpaRepository<WalkRecord, Long> {

}
