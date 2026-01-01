package me.minkyoung.buddy_back.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_message",
        indexes = @Index(name="idx_room_created", columnList="room_id, created_at")
)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Chat_Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(nullable = false,name = "room_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Chat_Room room;

    @JoinColumn(nullable = false,name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false, name="content")
    private String content;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
