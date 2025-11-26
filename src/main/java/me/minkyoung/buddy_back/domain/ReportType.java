package me.minkyoung.buddy_back.domain;

public enum ReportType {
    DISKLIKE("마음에 들지 않습니다."),
    VIOLENCE_HATE_ABUSE("폭력 또는 혐오 또는 학대"),
    ADVERTISEMENT("제품 판매 또는 홍보 행위"),
    NUDITY_OR_SEXUAL("나체 이미지 또는 선정적 내용"),
    INAPPROPRIATE_POSt_OR_COMMENT("부적절한 게시글 또는 댓글"),
    OTHER("기타");

    private String description;

    ReportType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
