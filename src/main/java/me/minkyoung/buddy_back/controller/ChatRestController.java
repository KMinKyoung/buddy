package me.minkyoung.buddy_back.controller;

import lombok.RequiredArgsConstructor;
import me.minkyoung.buddy_back.dto.*;
import me.minkyoung.buddy_back.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatRestController {
    private final ChatService chatService;

    // 내 방 목록
    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomListItemResponseDto>> myRooms(Principal principal) {
        String myEmail = principal.getName();
        return ResponseEntity.ok(chatService.getMyRoomsByEmail(myEmail));
    }

    // 방 메시지 히스토리
    @GetMapping("/rooms/{roomId}/messages")
    public ResponseEntity<ChatMessageListResponseDto> messages(@PathVariable Long roomId,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "30") int size,
                                                               Principal principal) {
        String myEmail = principal.getName();
        return ResponseEntity.ok(chatService.getMessageByEmail(roomId, myEmail, page, size));
    }

    // 1:1 방 생성/재사용
    @PostMapping("/rooms/direct")
    public ResponseEntity<ChatRoomResponseDto> directRoom(@RequestBody CreateRoomDirectRequestDto req,
                                                          Principal principal) {
        String myEmail = principal.getName();
        System.out.println("[CHAT] principal=" + principal.getName());
        return ResponseEntity.ok(chatService.getOrCreateDirectRoomByEmail(myEmail, req.getTargetUserId()));
    }

    // 그룹 방 생성
    @PostMapping("/rooms/group")
    public ResponseEntity<ChatRoomResponseDto> groupRoom(@RequestBody CreateGroupRoomRequestDto req,
                                                         Principal principal) {
        String myEmail = principal.getName();
        return ResponseEntity.ok(chatService.createGroupRoomByEmail(myEmail, req));
    }
}
