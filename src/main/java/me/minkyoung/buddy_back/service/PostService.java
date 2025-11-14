package me.minkyoung.buddy_back.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.minkyoung.buddy_back.dto.RequestPostDto;
import me.minkyoung.buddy_back.dto.ResponsePostDto;
import me.minkyoung.buddy_back.entity.Post;
import me.minkyoung.buddy_back.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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


    //글 조회, 추후 로그인 기능 추가 후 작성자까지 조회
    public ResponsePostDto getbyIdPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("게시물을 찾을 수 없습니다."));

        return  ResponsePostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .description(post.getDescription())
                .image_url(post.getImgUrl())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }


    //글 목록 조회(오름차순+생성일 순으로 정렬)
    public Page<ResponsePostDto> getByAllPost(Pageable pageable){

        Page<Post> posts = postRepository.findAllByOrderByCreatedAtDesc(pageable);

        return posts.map(post ->
                ResponsePostDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .description(post.getDescription())
                        .image_url(post.getImgUrl())
                        .createdAt(post.getCreatedAt())
                        .updatedAt(post.getUpdatedAt())
                        .build()
        );
    }

    //글 수정

    //글 삭제
}
