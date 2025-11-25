package me.minkyoung.buddy_back.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //자동증가
    private Long id;

    // 유저_id를 외래키로 받기
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    //게시글 Id를 외래키로 받기
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "description")
    private String description;

    @CreatedDate
    @Column(name = "created_at") //생성일
    private LocalDateTime createdAt;

    @LastModifiedDate //수정 시 자동 갱신
    @Column(name = "updated_at") //수정 년,월,일
    private LocalDateTime updatedAt;
}
