package me.minkyoung.buddy_back.repository;

import me.minkyoung.buddy_back.entity.Chat_Room_Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomMemberRepository extends JpaRepository<Chat_Room_Member,Long> {
    // 멤버인지 검증
    boolean existsByRoomIdAndUserId(Long roomId,Long userId);
    // 방의 멤버 목록 조회
    List<Chat_Room_Member> findByRoomId(Long roomId);
    //내 방 목록 조회
    List<Chat_Room_Member> findByUserId_Id(Long userId);
}
