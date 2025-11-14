package me.minkyoung.buddy_back.repository;


import me.minkyoung.buddy_back.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long> {

    //글 세부 조회
    Optional<Post> findById(Long Id);

    //글 목록 조회 -> 자동 정렬(프론트가 아닌 백엔드에서 Pageable을 이용한 정렬)
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    //내가 작성한 글 조회 -> 로그인 기능 추가 후 optional을통해 추가될 예정
}
