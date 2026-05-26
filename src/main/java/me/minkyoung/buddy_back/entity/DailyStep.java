package me.minkyoung.buddy_back.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(
        name = "daily_step",
        uniqueConstraints = {
            @UniqueConstraint(
                    name = "uk_daily_step_user_date",
                    columnNames = {"user_id", "step_date"}
            )
        }
)
public class DailyStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "step_date", nullable = false)
    private LocalDate stepDate;

    @Column(name = "step_count", nullable = false)
    private int stepCount;

    @Column(name = "goal_steps", nullable = false)
    private int goalSteps;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public static DailyStep create(User user, LocalDate stepDate, int stepCount){
        return DailyStep.builder()
                .user(user)
                .stepDate(stepDate)
                .stepCount(stepCount)
                .goalSteps(10000)
                .build();
    }

    public void updateStepCount(int stepCount){
        this.stepCount = stepCount;
    }
}
