package me.minkyoung.buddy_back.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.minkyoung.buddy_back.dto.CommentRequest;
import me.minkyoung.buddy_back.dto.CommentResponse;
import me.minkyoung.buddy_back.entity.Comment;
import me.minkyoung.buddy_back.entity.Post;
import me.minkyoung.buddy_back.entity.User;
import me.minkyoung.buddy_back.repository.CommentRepository;
import me.minkyoung.buddy_back.repository.PostRepository;
import me.minkyoung.buddy_back.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    //댓글 작성, 조회, 삭제, 수정 기능이 필요하다.

    //댓글 작성
    public CommentResponse addComment(Long postId, CommentRequest commentRequest, Authentication authentication) {
        String email = authentication.getName();
        User user =  userRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 사용자입니다."));


        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 게시물입니다."));

        //빌더 패턴을 이용해서 댓글 작성 진행
        Comment comment = Comment.builder()
                .description(commentRequest.getDescription())
                .build();

        comment.setUser(user);
        comment.setPost(post);

        //저장 후 반환
        commentRepository.save(comment);

        CommentResponse response = CommentResponse.builder()
                .id(comment.getId())
                .post_id(comment.getPost().getId())
                .user_id(comment.getUser().getId())
                .name(comment.getUser().getName())
                .description(comment.getDescription())
                .created_at(comment.getCreatedAt())
                .updated_at(comment.getUpdatedAt())
                .build();

        return response;
    }

    //댓글 전체 조회
    public List<CommentResponse> getAllComments(Long postId) {
        List<Comment> comments= commentRepository.findByPostIdOrderByCreatedAtAsc(postId);
        return comments.stream().map(
                        comment ->
                                CommentResponse.builder()
                                        .id(comment.getId())
                                        .post_id(comment.getPost().getId())
                                        .user_id(comment.getUser().getId())
                                        .name(comment.getUser().getName())
                                        .description(comment.getDescription())
                                        .created_at(comment.getCreatedAt())
                                        .updated_at(comment.getUpdatedAt())
                                        .build()
                )
                .toList();
    }

    //댓글 수정
    public CommentResponse updateComment(Long postId, Long commentId,CommentRequest commentRequest, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        if(!comment.getPost().getId().equals(postId)){
            throw new IllegalArgumentException("해당 게시글의 댓글이 아닙니다.");
        }

        if(!comment.getUser().getId().equals(user.getId())){
            throw new IllegalArgumentException("작성자 본인만 수정 가능합니다.");
        }

        comment.setDescription(commentRequest.getDescription());

        commentRepository.save(comment);

        return CommentResponse.builder()
                .id(comment.getId())
                .post_id(comment.getPost().getId())
                .user_id(comment.getUser().getId())
                .name(comment.getUser().getName())
                .description(comment.getDescription())
                .created_at(comment.getCreatedAt())
                .updated_at(comment.getUpdatedAt())
                .build();
    }

    //댓글 삭제 -> 이후 관리자에게도 권한 포함
    public void deleteComment(Long postId, Long commentId, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        if(!comment.getPost().getId().equals(postId)){
            throw new IllegalArgumentException("해당 게시글의 댓글이 아닙니다.");
        }

        if(!comment.getUser().getId().equals(user.getId())){
            throw new IllegalArgumentException("작성자 본인만 삭제 가능합니다.");
        }

        commentRepository.delete(comment);
    }
}
