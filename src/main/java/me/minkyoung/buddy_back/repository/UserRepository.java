package me.minkyoung.buddy_back.repository;

import me.minkyoung.buddy_back.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); //email을 통한 사용자 정보 가져오기

    boolean existsByEmail(String email); //이메일 존재 여부 확인

    Optional<User> findByProviderAndProviderId(String provider, String providerId);
}
