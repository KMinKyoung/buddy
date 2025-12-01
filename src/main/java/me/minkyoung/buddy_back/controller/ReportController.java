package me.minkyoung.buddy_back.controller;

import lombok.RequiredArgsConstructor;
import me.minkyoung.buddy_back.domain.ReportStatus;
import me.minkyoung.buddy_back.dto.ReportDetailDto;
import me.minkyoung.buddy_back.dto.ReportRequestDto;
import me.minkyoung.buddy_back.dto.ReportSummaryDto;
import me.minkyoung.buddy_back.service.ReportService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @PostMapping()
    public ResponseEntity<Void> createReport(Authentication authentication, @RequestBody ReportRequestDto requestDto) {
        reportService.CreateReport(authentication, requestDto);
        return ResponseEntity.ok().build();
    }

    //상세조회
    @GetMapping("/{id}")
    public ResponseEntity<ReportDetailDto> getReport(Authentication authentication, @PathVariable long id) {
        ReportDetailDto reportDetailDto = reportService.getReportById(id);
        return ResponseEntity.ok(reportDetailDto);
    }

    //전체 조회
    @GetMapping()
    public ResponseEntity<Page<ReportSummaryDto>> getAllReports(@RequestParam(required = false) ReportStatus status, Pageable pageable){
        Page<ReportSummaryDto> reportSummaryDtoPage = reportService.getAllReports(status,pageable);
        return ResponseEntity.ok(reportSummaryDtoPage);
    }
}
