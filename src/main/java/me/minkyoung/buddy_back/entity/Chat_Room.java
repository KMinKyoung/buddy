package me.minkyoung.buddy_back.entity;

import jakarta.persistence.*;
import lombok.*;
import me.minkyoung.buddy_back.domain.RoomType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_room")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Chat_Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //개인 채팅 또는 단체 채팅
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private RoomType type;

    @Column(length = 50)
    private String name;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;


}
