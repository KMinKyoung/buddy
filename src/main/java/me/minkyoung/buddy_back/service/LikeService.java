package me.minkyoung.buddy_back.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.minkyoung.buddy_back.dto.LikeToggleResponseDto;
import me.minkyoung.buddy_back.entity.Post;
import me.minkyoung.buddy_back.entity.PostsLikes;
import me.minkyoung.buddy_back.entity.User;
import me.minkyoung.buddy_back.repository.CommentLikeRepository;
import me.minkyoung.buddy_back.repository.PostLikeRepository;
import me.minkyoung.buddy_back.repository.PostRepository;
import me.minkyoung.buddy_back.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {
    private final PostLikeRepository postLikeRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    //게시글 좋아요
    public LikeToggleResponseDto postLike(Authentication authentication, Long postId){
        //1, 로그인한 사용자인가 예외 처리
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 사용자입니다."));
        //2. 존재하는 게시글인지 예외 처리
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 게시물입니다."));
        //3. assertPostNotLiked 예외 불러오기
        assertPostNotLiked(user.getId(), postId);
        //4. 저장 후 likeCount 재계산(count)
        PostsLikes likes = PostsLikes.builder()
                .user(user)
                .post(post)
                .build();
        postLikeRepository.save(likes);
        //5. 응답 DTO(liked, likeCount) 반환
        long likeCount = postLikeRepository.countByPostId(postId);
        return LikeToggleResponseDto.builder()
                .liked(true)
                .likeCount(likeCount)
                .build();
    }
    //게시글 좋아요 취소
    public LikeToggleResponseDto cancelPostLike(Authentication authentication, Long postId){
        //1, 로그인한 사용자인가 예외 처리
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 사용자입니다."));
        //2. 존재하는 게시글인지 예외 처리
       postRepository.findById(postId)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 게시물입니다."));

        //3. 내가 누른 좋아요 글인지 여부 확인(내 좋아요 여부 내부 메서드에서 예외처리)
        assertPostLiked(user.getId(),postId);
        //4. 모든 예외처리가 다 지나갓을 경우 delete처리
        postLikeRepository.deleteByUserIdAndPostId(user.getId(),postId);
        //5. likeCount 재계산
        long likeCount = postLikeRepository.countByPostId(postId);
        //6. 응답 DTO로 반환
        return LikeToggleResponseDto.builder()
                .liked(false)
                .likeCount(likeCount)
                .build();

    }

    //게시글에 대한 내 좋아요 여부 - 좋아요가 있을 경우 예외(생성전 중복 방지용)
    public void assertPostNotLiked(Long userId, Long postId){
        if(postLikeRepository.existsByUserIdAndPostId(userId,postId)){
            throw new IllegalArgumentException("이미 좋아요를 누른 상태입니다.");
        }
    }

    //게시글에 대한 내 좋아요 여부 - 좋아요가 없을 경우(취소 전 확인용)
    public void assertPostLiked(Long userId, Long postId){
        if(!postLikeRepository.existsByUserIdAndPostId(userId,postId)){
            throw new IllegalArgumentException("좋아요가 존재하지 않습니다.");
        }
    }
    //댓글 좋아요

    //댓글 좋아요 취소

    //댓글에 대한 내 좋아요 여부
    //댓글에 대한 내 좋아요 여부
}
