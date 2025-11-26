package me.minkyoung.buddy_back.entity;

import jakarta.persistence.*;
import lombok.*;
import me.minkyoung.buddy_back.domain.PenaltyStatus;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Date;

@Table(name = "penalty")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
//@Setter
@EntityListeners(AuditingEntityListener.class)
public class Penalty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "reason", length = 500)
    private String reason; //관리자가 확인하는 용도

    @Enumerated(EnumType.STRING)
    @Column(name = "penalty_status", nullable = false)
    private PenaltyStatus penaltyStatus;

    @Column(name = "start_at")
    private LocalDateTime startAt;

    @Column(name = "end_at")
    private LocalDateTime endAt;

    @CreatedDate //생성 시 자동 입력
    @Column(name = "created_at", updatable = false) //생성 년,월,일
    private LocalDateTime createdAt;

    //나중에 생각해보아야할 추가 방향 -> 제재 상세 페이지에서 연결된 신고 목록을 바로 보고싶으면 추가
}
