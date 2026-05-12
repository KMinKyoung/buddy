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
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@Transactional //트랜잭션 단위로 처리
@RequiredArgsConstructor

public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    //배포후 다시 이걸로 private final Optional<OciPostImageService>  ociPostImageService;
    private final OciPostImageService  ociPostImageService;

    // 글 조회 ,글 생성, 글 수정, 글 삭제 기능

    //글 생성
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
                .id(post.getId())
                .user_id(post.getUser().getId())
                .name(post.getUser().getName())
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
                .user_id(post.getUser().getId())
                .name(post.getUser().getName())
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
                        .user_id(post.getUser().getId())
                        .name(post.getUser().getName())
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
                .id(post.getId())
                .user_id(post.getUser().getId())
                .name(post.getUser().getName())
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

    public ResponsePostDto uploadPostImage(Long postId, MultipartFile file, Authentication authentication) throws Exception {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));


        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        //이미지 업로드 문제 확인용
        System.out.println("[UPLOAD] auth=" + (authentication == null ? "null" : authentication.getName()));
        System.out.println("[UPLOAD] postUserId=" + post.getUser().getId() + ", loginUserId=" + user.getId());


        if (!post.getUser().getId().equals(user.getId())) {
            throw new org.springframework.security.access.AccessDeniedException("해당 게시물의 이미지를 수정할 수 없습니다.");
        }

        if (file == null || file.isEmpty()) throw new IllegalArgumentException("파일이 없습니다.");
        if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("이미지 파일만 업로드 가능합니다.");
        }

        String objectKey = ociPostImageService.upload(postId, file);//->원문
       // 얘를 다시 String objectKey = ociPostImageService.orElseThrow(()->new IllegalArgumentException("OCI업로드는 로컬에서 불가능합니다."))
       //                 .upload(postId,file);
       // post.setImgUrl(objectKey); // DB에는 objectKey 저장



        return toResponse(post);
    }

    public String getPostImageParUrl(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));

        if (post.getImgUrl() == null || post.getImgUrl().isBlank()) {
            throw new IllegalArgumentException("이미지가 없습니다.");
        }
        return ociPostImageService.createReadParUrl(post.getImgUrl()); //원문 -> 아래 지우고 원래대로
        //return ociPostImageService
               // .orElseThrow(()->new IllegalArgumentException("로컬에서 불가능")).createReadParUrl(post.getImgUrl());
    }

    // 공통 응답 변환(핵심: objectKey 대신 조회용 URL 내려주기)
    private ResponsePostDto toResponse(Post post) {
        String imageUrl = (post.getImgUrl() == null || post.getImgUrl().isBlank())
                ? null
                : "/api/posts/" + post.getId() + "/image";

        return ResponsePostDto.builder()
                .id(post.getId())
                .user_id(post.getUser().getId())
                .name(post.getUser().getName())
                .title(post.getTitle())
                .description(post.getDescription())
                .image_url(imageUrl)
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

}
