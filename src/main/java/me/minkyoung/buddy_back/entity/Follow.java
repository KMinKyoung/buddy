package me.minkyoung.buddy_back.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "follows",
        uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_followeer_following",
                columnNames = {"follower_id","following_id"}
            )
        }
        )
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class) //자동 날짜 감시 기능 활성화
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //팔로우 사용자 1 : Follow N
    @JoinColumn(nullable = false, name = "follower_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User follower;

    //팔로우 대상자 1 : Follow N
    @JoinColumn(nullable = false, name = "following_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User following;

    @CreatedDate //생성 시 자동 입력
    @Column(name = "created_at") //생성 년,월,일
    private LocalDateTime createdAt;

}
