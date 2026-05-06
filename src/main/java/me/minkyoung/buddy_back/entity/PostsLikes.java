package me.minkyoung.buddy_back.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "posts_likes")
@Entity
@Getter
@Setter
public class PostsLikes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //(1)사람은 (N)여러개의 좋아요를 할 수 있다
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY) // (1)게시글은 (N) 여러개의 좋아요를 받을 수 있다
    @JoinColumn(name = "post_id", nullable = false)
    private  Post post;

}
