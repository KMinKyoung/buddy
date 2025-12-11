package me.minkyoung.buddy_back.repository;

import me.minkyoung.buddy_back.domain.ReportStatus;
import me.minkyoung.buddy_back.domain.ReportType;
import me.minkyoung.buddy_back.entity.Report;
import me.minkyoung.buddy_back.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {

    Page<Report> findByStatus(ReportStatus status, Pageable pageable); //상태별로 필터링해서 확인(Pending)

    boolean existsByReporterIdAndReportedUserIdAndReportType(Long reporterId,Long targetUserId, ReportType reportType);

    //오늘 하루 사이에 들어온 신고 수
    int countByReportedUserAndCreatedAtBetween(User reportedUser, LocalDateTime start, LocalDateTime end);

    //최근 신고 10개
    List<Report> findTop10ByReportedUserOrderByCreatedAtDesc(User reportedUser);
}
