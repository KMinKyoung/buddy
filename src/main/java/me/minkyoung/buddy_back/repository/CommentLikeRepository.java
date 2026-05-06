package me.minkyoung.buddy_back.repository;

import me.minkyoung.buddy_back.entity.CommentsLikes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentsLikes,Long> {
    //댓글 좋아요 수
    long countByCommentId(Long commentId);

    // 내가 좋아요한 여부
    boolean existsByUserIdAndCommentId(Long userId, Long commentId);

    //좋아요 취소
    void deleteByUserIdAndCommentId(Long userId, Long commentId);
}
