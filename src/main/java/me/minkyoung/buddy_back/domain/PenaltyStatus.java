package me.minkyoung.buddy_back.domain;

public enum PenaltyStatus {
    NONE, //아무런 제재도 받지않는 단계
    WAITING, //경고만 주는 단계
    LIMITED, // 기능 제한을 주는 단계
    TEMP_BAN, //일정 기간 동안 정지 단계
    PERM_BAN //영구 정지 단계
}
