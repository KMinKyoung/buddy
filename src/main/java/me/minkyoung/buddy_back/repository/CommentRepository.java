package me.minkyoung.buddy_back.repository;

import me.minkyoung.buddy_back.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    //해당 게시글에 대한 모든 댓글을 생성일 기준 오름차순으로 정렬하여 조회
    List<Comment> findByPostIdOrderByCreatedAtAsc(Long postId);

    //내가 작성한 댓글 조회
    Page<Comment> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}
