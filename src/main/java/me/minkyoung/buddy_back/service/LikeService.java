package me.minkyoung.buddy_back.service;

import lombok.RequiredArgsConstructor;
import me.minkyoung.buddy_back.dto.LikeToggleResponseDto;
import me.minkyoung.buddy_back.dto.ResponsePostDto;
import me.minkyoung.buddy_back.entity.Post;
import me.minkyoung.buddy_back.entity.PostsLikes;
import me.minkyoung.buddy_back.entity.User;
import me.minkyoung.buddy_back.repository.CommentLikeRepository;
import me.minkyoung.buddy_back.repository.PostLikeRepository;
import me.minkyoung.buddy_back.repository.PostRepository;
import me.minkyoung.buddy_back.repository.UserRepository;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        boolean alraedyLiked = postLikeRepository.existsByUserIdAndPostId(user.getId(), postId);

        //3. 좋아요 여부 확인
        if(alraedyLiked){
            //3-1. 이미 있으면 취소
            postLikeRepository.deleteByUserIdAndPostId(user.getId(), postId);
        }else {
            //3-2. 좋아요가 없으면 생성
            //저장 후 likeCount 재계산(count)
            PostsLikes likes = PostsLikes.builder()
                    .user(user)
                    .post(post)
                    .build();
            postLikeRepository.save(likes);
        }

        //5. 응답 DTO(liked, likeCount) 반환
        long likeCount = postLikeRepository.countByPostId(postId);
        return LikeToggleResponseDto.builder()
                .liked(!alraedyLiked)
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

        //3. 좋아요가 있으면 삭제, 없으면 그냥 통과
        postLikeRepository.deleteByUserIdAndPostId(user.getId(),postId);
        //4. likeCount 재계산
        long likeCount = postLikeRepository.countByPostId(postId);
        //5. 응답 DTO로 반환
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
