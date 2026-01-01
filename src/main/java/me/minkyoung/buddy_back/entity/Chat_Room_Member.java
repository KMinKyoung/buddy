package me.minkyoung.buddy_back.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_room_member",
        uniqueConstraints = @UniqueConstraint(name = "uk_room_user",
                columnNames = {"room_id","user_id"})) //중복 가입 방지
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Chat_Room_Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(nullable = false,name = "room_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Chat_Room room;

    @JoinColumn(nullable = false, name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @CreatedDate
    @Column(name="joined_at", nullable = false, updatable = false)
    private LocalDateTime joinedAt;
}
