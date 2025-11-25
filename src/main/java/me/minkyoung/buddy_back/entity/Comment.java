package me.minkyoung.buddy_back.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Setter
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

    @Column(name = "created_at") //생성일
    private LocalDateTime createdAt;

    //댓글 수정일이 필요할까?
}
