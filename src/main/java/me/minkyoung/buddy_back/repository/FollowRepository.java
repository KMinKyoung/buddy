package me.minkyoung.buddy_back.repository;

import me.minkyoung.buddy_back.entity.Follow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    // 내가 상대를 팔로우 중인지 확인
    boolean existsByFollowIdAndFollowingId(Long followId, Long followingId);

    // 언팔로우할 때 기존 Follow row 찾기
    Optional<Follow> findByFollowIdAndFollowingId(Long followId, Long followingId);

    // 언팔로우 처리
    void deleteByFollowIdAndFollowingId(Long followId, Long followingId);

    // 해당 사용자를 팔로우하는 사람 수(팔로워 수)
    int countByFollowingId(Long followingId);

    // 해당 사용자가 팔로우하는 사람 수(팔로잉 수)
    int countByFollowerId(Long followerId);

    // 팔로잉 목록
    Page<Follow> findByFollowerId(Long followerId, Pageable pageable);

    // 팔로워 목록
    Page<Follow> findByFollowingId(Long followingId, Pageable pageable);

}
