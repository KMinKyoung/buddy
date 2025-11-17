package me.minkyoung.buddy_back.config.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import me.minkyoung.buddy_back.entity.User;
import me.minkyoung.buddy_back.jwt.JwtProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TokenProvider {
    private final JwtProperties jwtProperties;

    public String generateToken(User user, Duration expiredAt){
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }

    private String makeToken(Date expiry, User user){
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) //헤더 타입 : JWT
                .setIssuer(jwtProperties.getIssuer()) // ~@gmail.com(propertise 차일에서 설정한 값)
                .setIssuedAt(now) // 현재 시간
                .setExpiration(expiry) // expiry 멤버 변숫값
                .setSubject(user.getEmail()) // 유저 이메일
                .claim("id",user.getId()) // 유저 ID
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    //토큰 유효성 검증
    public boolean validateToken(String token){
        try{
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey()) //비밀값으로 복호화
                    .parseClaimsJws(token);

            return true;
        } catch(Exception e){ //복ㅎ화 과정에서 에러가 나면 유효하지 않는 토큰
            return false;
        }
    }

    //토큰 기반으로 인증 정보 가져오기
    public Authentication getAuthentication(String token){
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(new org.springframework.
                security.core.userdetails.User(claims.getSubject(),"",authorities),token,authorities);
    }

    //토큰 기반으로 유저 ID 가져오기
    public Long getUserId(String token){
        Claims claims = getClaims(token);
        return claims.get("id",Long.class);
    }

    private Claims getClaims(String token){
        return Jwts.parser() // 클레임 조회
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }
}
