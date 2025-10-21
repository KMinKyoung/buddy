package me.minkyoung.buddy_back.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.minkyoung.buddy_back.dto.RequestPostDto;
import me.minkyoung.buddy_back.dto.ResponsePostDto;
import me.minkyoung.buddy_back.entity.Post;
import me.minkyoung.buddy_back.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
@Transactional //트랜잭션 단위로 처리
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    // 글 조회 ,글 생성, 글 수정, 글 삭제

    //글 생성 -> 유저 값 받아오기 + 이미지 처리 추후에
    public ResponsePostDto createPost(RequestPostDto requestPostDto) {
        Post post = Post.builder()
                .title(requestPostDto.getTitle())
                .description(requestPostDto.getDescription())
                .build();

        postRepository.save(post);

        ResponsePostDto response = ResponsePostDto.builder()
                .title(post.getTitle())
                .description(post.getDescription())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();

        return response;
    }
}
