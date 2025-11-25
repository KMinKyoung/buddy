package me.minkyoung.buddy_back.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Table(name = "post")
@NoArgsConstructor //기본 생성자 생성
@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class) //자동 날짜 감시 기능 활성화
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //pk 설정 및 id 자동 증가
    private Long id;

    // 유저_id를 외래키로 받기
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "title") //제목
    private String title;

    @Column(name = "description") //내용
    private String description;

    @Column(name = "img_url") //이미지
    private String imgUrl;

    @CreatedDate //생성 시 자동 입력
    @Column(name = "created_at") //생성 년,월,일
    private LocalDateTime createdAt;

    @LastModifiedDate //수정 시 자동 갱신
    @Column(name = "updated_at") //수정 년,월,일
    private LocalDateTime updatedAt;


}
