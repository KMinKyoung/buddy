package me.minkyoung.buddy_back.domain;

public enum ReportStatus {
    PENDING, //보류상태, 신고가 들어온 직후의 상태
    COUNTED, //자동 제재 계산에 사용
    IGNORED, //기각/무시
    FALSE_REPORT // 악성/허위 신고
}
