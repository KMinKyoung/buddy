package me.minkyoung.buddy_back.repository;

import me.minkyoung.buddy_back.domain.Provider;
import me.minkyoung.buddy_back.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); //email을 통한 사용자 정보 가져오기

    boolean existsByEmail(String email); //이메일 존재 여부 확인

    Optional<User> findByProviderAndProviderId(Provider provider, String providerId);

    // 이름으로 검색해서 채팅방 생성
    @Query("""
        select u
        from User u
        where u.id <> :myUserId
        and lower(u.name) like lower(concat('%', :keyword, '%'))
    """)
    List<User> searchByName(
            @Param("keyword") String keyword,
            @Param("myUserId") Long myUserId,
            Pageable pageable
    );
}
