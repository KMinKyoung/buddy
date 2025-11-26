package me.minkyoung.buddy_back.entity;

import jakarta.persistence.*;
import lombok.*;
import me.minkyoung.buddy_back.domain.ReportStatus;
import me.minkyoung.buddy_back.domain.ReportType;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "report")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EntityListeners(AuditingEntityListener.class) //자동 날짜 감시 기능 활성화
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 신고자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    private User reporter;

    //신고 대상 유저
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_user_id",nullable = false)
    private User reportedUser;

    //근거가 된 제재(나중에 잘못 접수 된 신고에 대한 답변 및 해결을 해줄 수 있음)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "penalty_id")
    private Penalty penalty;

    @Enumerated(EnumType.STRING)
    @Column(name = "report_Type",nullable = false)
    private ReportType reportType;

    @Column(name = "reason")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "status")
    private ReportStatus status = ReportStatus.COUNTED;
}
