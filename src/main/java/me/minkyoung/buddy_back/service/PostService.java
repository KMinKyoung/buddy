package me.minkyoung.buddy_back.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.minkyoung.buddy_back.dto.RequestPostDto;
import me.minkyoung.buddy_back.dto.ResponsePostDto;
import me.minkyoung.buddy_back.entity.Post;
import me.minkyoung.buddy_back.entity.User;
import me.minkyoung.buddy_back.repository.PostRepository;
import me.minkyoung.buddy_back.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@Transactional //트랜잭션 단위로 처리
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 글 조회 ,글 생성, 글 수정, 글 삭제 기능

    //글 생성 -> 이미지 처리 추후에
    public ResponsePostDto createPost(RequestPostDto requestPostDto, Authentication authentication) {

        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Post post = Post.builder()
                .title(requestPostDto.getTitle())
                .description(requestPostDto.getDescription())
                .build();

        post.setUser(user);

        postRepository.save(post);

        ResponsePostDto response = ResponsePostDto.builder()
                .title(post.getTitle())
                .description(post.getDescription())
                .image_url(post.getImgUrl())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();

        return response;
    }

    //글 조회
    public ResponsePostDto getbyIdPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));

        return ResponsePostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .description(post.getDescription())
                .image_url(post.getImgUrl())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    //글 목록 조회(오름차순+생성일 순으로 정렬)
    public Page<ResponsePostDto> getByAllPost(Pageable pageable) {

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

    //글 수정, 추후 본인이 작성한 글에 대한 수정이 가능하도록 변경
    public ResponsePostDto updatePost(Long id, RequestPostDto requestPostDto, Authentication  authentication) {
        Post post = postRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 게시물입니다."));

        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 사용자입니다."));

        //본인 확인
        if(!post.getUser().getId().equals(user.getId())) {
            throw new org.springframework.security.access.AccessDeniedException("해당 게시물을 수정할 수 없습니다.");
        }

        //제목, 내용, 이미지 수정
        post.setTitle(requestPostDto.getTitle());
        post.setDescription(requestPostDto.getDescription());
        post.setImgUrl(requestPostDto.getImage_url());

        ResponsePostDto response = ResponsePostDto.builder()
                .title(post.getTitle())
                .description(post.getDescription())
                .image_url(post.getImgUrl())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();

        return response;
    }

    //글 삭제, 추후 관리자도 삭제 가능하도록
    public void deletePost(Long id,Authentication authentication) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));

        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                        .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        //본인 확인
        if(!post.getUser().getId().equals(user.getId())) {
            throw new org.springframework.security.access.AccessDeniedException("해당 게시물을 삭제할 수 없습니다.");
        }

        postRepository.delete(post);
    }
}
