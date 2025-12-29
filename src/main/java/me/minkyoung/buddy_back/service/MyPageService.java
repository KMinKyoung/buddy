package me.minkyoung.buddy_back.service;

import lombok.RequiredArgsConstructor;
import me.minkyoung.buddy_back.dto.MyCommentResponseDto;
import me.minkyoung.buddy_back.dto.MyPostResponseDto;
import me.minkyoung.buddy_back.dto.ResponsePostDto;
import me.minkyoung.buddy_back.entity.Comment;
import me.minkyoung.buddy_back.entity.Post;
import me.minkyoung.buddy_back.entity.User;
import me.minkyoung.buddy_back.repository.CommentRepository;
import me.minkyoung.buddy_back.repository.PostRepository;
import me.minkyoung.buddy_back.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final OciPostImageService ociPostImageService;

    //내 글 조회
    public Page<MyPostResponseDto> getById(Pageable pageable, Authentication authentication){
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Page<Post> posts =postRepository.findByUserIdOrderByCreatedAtDesc(user.getId(), pageable);

        return posts.map(post ->
                MyPostResponseDto.builder()
                        .id(post.getId())
                        .user_id(post.getUser().getId())
                        .name(post.getUser().getName())
                        .title(post.getTitle())
                        .description(post.getDescription())
                        .image_url(post.getImgUrl())
                        .createdAt(post.getCreatedAt())
                        .updatedAt(post.getUpdatedAt())
                        .build());

    }

    //내가 작성한 댓글 조회
    public Page<MyCommentResponseDto> getCommentById(Pageable pageable, Authentication authentication){
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 사용자입니다."));


        Page<Comment> comments = commentRepository.findByUserIdOrderByCreatedAtDesc(user.getId(),pageable);

        return comments.map(comment ->
                MyCommentResponseDto.builder()
                        .id(comment.getId())
                        .post_id(comment.getPost().getId())
                        .user_id(comment.getUser().getId())
                        .name(comment.getUser().getName())
                        .description(comment.getDescription())
                        .created_at(comment.getCreatedAt())
                        .updated_at(comment.getUpdatedAt())
                        .build());
    }

    //내가 좋아요한 글 조회
}
