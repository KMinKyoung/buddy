package me.minkyoung.buddy_back.entity;

import jakarta.persistence.*;
import lombok.*;
import me.minkyoung.buddy_back.domain.PenaltyStatus;
import me.minkyoung.buddy_back.domain.Provider;
import me.minkyoung.buddy_back.domain.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Builder
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "current_penalty_status",nullable = false)
    private PenaltyStatus currentPenaltyStatus = PenaltyStatus.NONE;

    @Column(name = "penalty_end_at")
    private LocalDateTime penaltyEndAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false)
    private Provider provider;

    private String providerId;

    public void updatePenaltyStatus(PenaltyStatus penaltyStatus, LocalDateTime endAt) {
        this.currentPenaltyStatus = penaltyStatus;
        this.penaltyEndAt = endAt;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override //계정 만료 여부 반환
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override //계정 잠금 여부 반환
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override //패스워드 만료 여부 반환
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override //계정 사용 가능 여부 반환
    public boolean isEnabled() {
        return true;
    }

}
