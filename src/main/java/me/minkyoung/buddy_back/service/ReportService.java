package me.minkyoung.buddy_back.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.minkyoung.buddy_back.domain.ReportStatus;
import me.minkyoung.buddy_back.dto.ReportDetailDto;
import me.minkyoung.buddy_back.dto.ReportRequestDto;
import me.minkyoung.buddy_back.dto.ReportSummaryDto;
import me.minkyoung.buddy_back.entity.Report;
import me.minkyoung.buddy_back.entity.User;
import me.minkyoung.buddy_back.repository.ReportRepository;
import me.minkyoung.buddy_back.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportService {
    private final UserRepository userRepository;
    private final ReportRepository reportRepository;
    private final PenaltyService penaltyService;

    //신고 생성, 전체 및 상세 조회(관리자)

    //신고 생성
    public void CreateReport(Authentication authentication, ReportRequestDto requestDto) { //타겟을 매개변수로 받음
        //신고한 사용자가 실존하는지 + 신고자가 중복 신고인지 확인
        String email = authentication.getName();
        User reporter = userRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        //매개변수로 받은 타겟이 실제로 존재하는지 확인
        User targetUser = userRepository.findById(requestDto.getReportedUserId())
                .orElseThrow(()->new IllegalArgumentException("신고대상 사용자가 존재하지 않습니다."));

        boolean exists =  reportRepository.existsByReporterIdAndReportedUserIdAndReportType(
                reporter.getId(),
                targetUser.getId(),
                requestDto.getReportType()
        );


        //3일을 간격으로 리셋되도록 수정
        if(exists){
            throw new IllegalArgumentException("이미 동일한 내용으로 신고한 이력이 존재합니다.");
        }

        //신고 엔터티 생성
        Report report = Report.builder()
                .reporter(reporter)
                .reportedUser(targetUser)
                .reportType(requestDto.getReportType())
                .reason(requestDto.getReason())
                .status(ReportStatus.PENDING)
                .build();
        //saved으로 db에 저장
        reportRepository.save(report);

        //제재 적용
        penaltyService.applyPenaltyRules(targetUser.getId());

    }

    //신고 상세 조회
    public ReportDetailDto getReportById(Long id){
        Report report =reportRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("신고를 찾을 수 없습니다."));

        // 제재가 없을 경우
        Long penaltyId = null;
        if(report.getPenalty() != null){
            penaltyId = report.getPenalty().getId();
        }

        return ReportDetailDto.builder()
                .id(report.getId())
                .penaltyId(penaltyId) //신고와 연결된 제재
                .reporterId(report.getReporter().getId())//신고자
                .reporterEmail(report.getReporter().getEmail())
                .reportedUserId(report.getReportedUser().getId())//대상자
                .reportedUserEmail(report.getReportedUser().getEmail())
                .reportType(report.getReportType())
                .reason(report.getReason())
                .reportStatus(report.getStatus())
                .build();
    }

    //신고 목록 조회(상태:Pending)
    public Page<ReportSummaryDto> getAllReports(ReportStatus status,Pageable pageable){
        Page<Report> reports;

        if(status == null){
            //상태 필터 없이 전체 조회
            reports = reportRepository.findAll(pageable);
        } else{
            //특정 상태만 필터링
            reports = reportRepository.findByStatus(status,pageable);
        }

        return reports.map(report ->
                ReportSummaryDto.builder()
                        .id(report.getId())
                        .reportType(report.getReportType())
                        .reportStatus(report.getStatus())
                        .build()
        );
    }

}
