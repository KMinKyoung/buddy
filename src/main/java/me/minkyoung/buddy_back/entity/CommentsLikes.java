package me.minkyoung.buddy_back.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "commets_likes")
@Entity
public class CommentsLikes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //1:N관계 1명이 여러개를 좋아요할 수 있음
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY) // 1:N 관계 1명이 여러 댓글을 좋아요할 수 있음
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;
}
