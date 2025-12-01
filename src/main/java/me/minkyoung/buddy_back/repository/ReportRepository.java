package me.minkyoung.buddy_back.repository;

import me.minkyoung.buddy_back.domain.ReportStatus;
import me.minkyoung.buddy_back.domain.ReportType;
import me.minkyoung.buddy_back.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {

    Page<Report> findByStatus(ReportStatus status, Pageable pageable); //상태별로 필터링해서 확인(Pending)

    boolean existsByReporterIdAndReportedUserIdAndReportType(Long reporterId,Long targetUserId, ReportType reportType);
}
