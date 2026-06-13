package me.minkyoung.buddy_back.repository;


import me.minkyoung.buddy_back.entity.Post;
import me.minkyoung.buddy_back.entity.PostsLikes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostLikeRepository extends JpaRepository<PostsLikes,Long> {

    //모든 사용자의 좋아요 개수
    long countByPostId(Long postId);

    // 내 좋아요 여부
    boolean existsByUserIdAndPostId(Long userId, Long postId);


    //좋아요 취소
    void deleteByUserIdAndPostId(Long userId, Long postId);

    //마이페이지 기준 2개
    //내가 좋아요한 post ID페이지로 가져오기(페이징 + 오름차순 정렬)
    @Query("select p.id from PostsLikes pl join pl.post p where pl.user.id =:userId order by p.createdAt asc")
    Page<Long> findMyLikedPostIds(@Param("userId") Long userId, Pageable pageable);

    //ID들로 Post정보 한번에 가져오기
    @Query("select p from Post p join fetch p.user where p.id in :postIds")
    List<Post> findPostsWithUserByIds(@Param("postIds") List<Long> postIds);

    //post 피드 기준 2개
    //post id별 좋아요 개수
    @Query("select pl.post.id, count(pl.id)\n" +
            "    from PostsLikes pl\n" +
            "    where pl.post.id in :postIds\n" +
            "    group by pl.post.id")
    List<Object[]> countLikesByPostIds(@Param("postIds") List<Long> postIds);

    // 내가 좋아요한 postId를 한번에
    @Query("select pl.post.id\n" +
            "        from PostsLikes pl\n" +
            "        where pl.user.id = :userId\n" +
            "          and pl.post.id in :postIds")
    List<Long> findLikedPostIdsByUser(@Param("userId") Long userId, @Param("postIds") List<Long> postIds);
}
