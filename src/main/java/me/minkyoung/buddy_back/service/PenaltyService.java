package me.minkyoung.buddy_back.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.minkyoung.buddy_back.domain.PenaltyStatus;
import me.minkyoung.buddy_back.dto.PenaltyDetailDto;
import me.minkyoung.buddy_back.dto.PenaltySummaryDto;
import me.minkyoung.buddy_back.dto.ReportSummaryDto;
import me.minkyoung.buddy_back.entity.Penalty;
import me.minkyoung.buddy_back.entity.Report;
import me.minkyoung.buddy_back.entity.User;
import me.minkyoung.buddy_back.repository.PenaltyRepository;
import me.minkyoung.buddy_back.repository.ReportRepository;
import me.minkyoung.buddy_back.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PenaltyService {
    private final UserRepository userRepository;
    private final PenaltyRepository penaltyRepository;//자동 제재 기준 계산용도
    private final ReportRepository reportRepository;

    //기준이 넘었을 경우 패널티 엔터티를 생성하는 역할
    public Optional<PenaltyDetailDto> applyPenaltyRules(Long targetUserId){
        // 받은 타겟 유저가 유효한 사용자인지 확인 userRepository.findById(tartgetuser.getId) 예외문 ("존재하지 않는 사용자입니다.")
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 사용자입니다."));

        //현재 제재 상태
        PenaltyStatus currentStatus = targetUser.getCurrentPenaltyStatus();

        //오늘 하루 동안 받은 신고 수 계산
        LocalDate today = LocalDate.now();
        LocalDateTime todayStart = today.atStartOfDay();  //오늘 0시
        LocalDateTime tomorrowStart = today.plusDays(1).atStartOfDay(); //내일 0시
        LocalDateTime now =LocalDateTime.now();

        int todayReportCount = reportRepository.countByReportedUserAndCreatedAtBetween(
                targetUser, todayStart, tomorrowStart
        );

        //기준을 넘지 못하면 제재 없음 -> 종료 /기준이 없으면 제재가 없는 로직이였나??
        if(currentStatus != PenaltyStatus.NONE || todayReportCount < 5){
            return Optional.empty();
        }

        //기준을 넘을 경우(None + 신고 5건 이상) -> 그거가 될 최근 신고 몇 개를 가져옴
        List<Report> recentReports = reportRepository.findTop10ByReportedUserOrderByCreatedAtDesc(targetUser);

        //Warning 제재로 새로 생성하고, 저장 + User 상태 반영 + DTO 변환
        PenaltyDetailDto dto = createPenalty(
            targetUser,
            PenaltyStatus.WARNING,
            "하루 동안 다수의 신고가 접수됭 경고가 부여되었습니다.",
            now, //startAt
            null, //경고의 경우 기간이 없음
            recentReports
        );

        return Optional.of(dto);
    }

    public PenaltyDetailDto createPenalty(User targetUser, PenaltyStatus nextStatus, String reason, LocalDateTime startAt, LocalDateTime endAt, List<Report> basisReports){
        if(basisReports == null || basisReports.isEmpty()){
            throw new IllegalArgumentException("제재의 근거가 되는 신고가 최소 1개 이상 있어야 합니다.");
        }

        Report mainReport = basisReports.get(0);

        //엔티티 생성
        Penalty penalty = Penalty.builder()
                .user(targetUser)
                .penaltyStatus(nextStatus)
                .startAt(startAt)
                .endAt(endAt)
                .reason(reason)
                .report(mainReport)
                .build();

        Penalty saved = penaltyRepository.save(penalty);

        //User의 현재 제재 상태도 갱신
        targetUser.updatePenaltyStatus(nextStatus,endAt);

        //Report -> ReportSummay / Penalty -> PenaltyDetail로 변환
        List<ReportSummaryDto> reportSummaryDtos = basisReports.stream()
                .map(report -> ReportSummaryDto.from(report))
                .toList();

        return PenaltyDetailDto.from(saved,reportSummaryDtos);
    }

    //제제 조회(세부/요약) 기능
}
