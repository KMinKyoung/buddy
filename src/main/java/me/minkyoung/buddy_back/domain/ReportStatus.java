package me.minkyoung.buddy_back.domain;

public enum ReportStatus {
    COUNTED, //자동 제재 계산에 사용
    IGNORED, //기각/무시
    FALSE_REPORT // 악성/허위 신고
}
