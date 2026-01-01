package me.minkyoung.buddy_back.repository;

import me.minkyoung.buddy_back.entity.Chat_Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<Chat_Message,Long> {
    //1. 방 히스토리 조회
    Page<Chat_Message> findByRoomIdOrderByCreatedAtAsc(Long roomId, Pageable pageable);
    //2. 마지막 메시지 1개 조회(방 목록용)
    Optional<Chat_Message> findByRoomIdOrderByCreatedAtDesc(Long roomId);
}
