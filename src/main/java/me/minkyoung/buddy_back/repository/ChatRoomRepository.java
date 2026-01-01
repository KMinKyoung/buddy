package me.minkyoung.buddy_back.repository;

import me.minkyoung.buddy_back.entity.Chat_Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<Chat_Room,Long> {
    // 1:1일 경우 두 유저로 Direct 방이 이미 있으면 재사용을 위한 조회
    @Query("""
        select r from Chat_Room r
        join Chat_Room_Member m1 on m1.room = r and m1.user.id = :userId1
        join Chat_Room_Member m2 on m2.room = r and m2.user.id = :userId2
        where r.type = me.minkyoung.buddy_back.domain.RoomType.DIRECT
    """)
    // 조회 결과가 존재하면 반환, 조회 결과가 없으면 널값
    Optional<Chat_Room> findDirectRoomByTwoUsers(@Param("userId1") Long userId1,
                                                 @Param("userId2") Long userId2);
}

