package me.minkyoung.buddy_back.service;

import lombok.RequiredArgsConstructor;
import me.minkyoung.buddy_back.domain.RoomType;
import me.minkyoung.buddy_back.dto.*;
import me.minkyoung.buddy_back.entity.Chat_Message;
import me.minkyoung.buddy_back.entity.Chat_Room;
import me.minkyoung.buddy_back.entity.Chat_Room_Member;
import me.minkyoung.buddy_back.entity.User;
import me.minkyoung.buddy_back.repository.ChatMessageRepository;
import me.minkyoung.buddy_back.repository.ChatRoomMemberRepository;
import me.minkyoung.buddy_back.repository.ChatRoomRepository;
import me.minkyoung.buddy_back.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {
    //구현해야할 서비스
    //1. 방 생성/조회(1:1일 경우 재사용 포함
    //  - 1:1 -> 두 유저가 이미 방이 있으면 재사용, 없으면 생성
    //  - 그룹 : 방생성 + 멤버들 추가
    //2. 방 멤버심 검증(권한 체크)
    //  - 채팅을 보낸자가 방 멤버인가, 로그인한 사용자인지 등 확실한 방 멤버인지 확인
    //3. 메시지 저장
    //  - 재접속/새로고챔. 재실행 시 채팅이 살아있어야함
    //4. 메시지 브로드캐스트
    //5. 메시지 히스토리 조회
    //  - 멤버 검증 후 조회하기 때문에 웹소켓으로 실시간성을 챙기고, 과거에 작성한 메시지는 HTTP로 조회
    //※ 유의 사항 : 메시지 입력에 대한 검증, 식별자(roomId,userId)입력 검증, 권한 입력 검증이 이뤄져야함

   private final ChatMessageRepository chatMessageRepository;
   private final ChatRoomRepository chatRoomRepository;
   private final ChatRoomMemberRepository chatRoomMemberRepository;
   private final UserRepository userRepository;
   private final SimpMessagingTemplate simpMessagingTemplate;


    public ChatRoomResponseDto getOrCreateDirectRoomByEmail(String myEmail, Long targetUserId) {
        Long myUserId = getUserIdByEmailOrThrow(myEmail);
        return getOrCreateDirectRoom(myUserId, targetUserId);
    }

    public ChatRoomResponseDto createGroupRoomByEmail(String myEmail, CreateGroupRoomRequestDto requestDto) {
        Long myUserId = getUserIdByEmailOrThrow(myEmail);
        return createGroupRoom(myUserId, requestDto);
    }

    public ChatMessageBroadcastResponseDto sendMessageByEmail(Long roomId, String senderEmail, ChatMessageSendRequestDto requestDto) {
        Long senderUserId = getUserIdByEmailOrThrow(senderEmail);
        return sendMessage(roomId, senderUserId, requestDto);
    }

    @Transactional(readOnly = true)
    public ChatMessageListResponseDto getMessageByEmail(Long roomId, String myEmail, int page, int size) {
        Long myUserId = getUserIdByEmailOrThrow(myEmail);
        return getMessage(roomId, myUserId, page, size);
    }

    @Transactional(readOnly = true)
    public List<ChatRoomListItemResponseDto> getMyRoomsByEmail(String myEmail) {
        Long myUserId = getUserIdByEmailOrThrow(myEmail);
        return getMyRooms(myUserId);
    }

    //1:1방 생성
    public ChatRoomResponseDto getOrCreateDirectRoom(Long myUserId, Long targetUserId){
        //이미 존재하는 대화방이면 가져오기, 없으면 생성
        //user 존재 검증
        if (myUserId == null || targetUserId == null) {
            throw new IllegalArgumentException("유저 정보가 올바르지 않습니다.");
        }
        if (myUserId.equals(targetUserId)) {
            throw new IllegalArgumentException("자기 자신과의 채팅방은 만들 수 없습니다.");
        }

        User me = getUserOrThrow(myUserId);
        User target = getUserOrThrow(targetUserId);

        return chatRoomRepository.findDirectRoomByTwoUsers(myUserId, targetUserId)
                .map(room -> ChatRoomResponseDto.builder()
                        .roomId(room.getId())
                        .type(room.getType().name())
                        .name(room.getName())
                        .createdAt(room.getCreatedAt())
                        .build())
                .orElseGet(() -> {
                    Chat_Room savedRoom = chatRoomRepository.save(
                            Chat_Room.builder().type(RoomType.DIRECT).name(null).build()
                    );

                    chatRoomMemberRepository.saveAll(List.of(
                            Chat_Room_Member.builder().room(savedRoom).user(me).build(),
                            Chat_Room_Member.builder().room(savedRoom).user(target).build()
                    ));

                    return ChatRoomResponseDto.builder()
                            .roomId(savedRoom.getId())
                            .type(savedRoom.getType().name())
                            .name(savedRoom.getName())
                            .createdAt(savedRoom.getCreatedAt())
                            .build();
                });
    }

    //그룹방 생성
    public ChatRoomResponseDto createGroupRoom(Long ownerId, CreateGroupRoomRequestDto requestDto){
        // 이름과 멤버 아이디(id) 입력 검증
        if (ownerId == null) throw new IllegalArgumentException("owner 정보가 없습니다.");
        if (requestDto == null) throw new IllegalArgumentException("요청값이 없습니다.");

        String name = (requestDto.getName() == null) ? null : requestDto.getName().trim();
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("방 이름이 비었습니다.");
        if (name.length() > 50) throw new IllegalArgumentException("방 이름은 최대 50자입니다.");

        List<Long> memberIds = requestDto.getMemberIds();
        if (memberIds == null || memberIds.isEmpty()) throw new IllegalArgumentException("멤버가 없습니다.");


        // owner 포함 보장 + 중복 제거
        Set<Long> unique = new LinkedHashSet<>(memberIds);
        unique.add(ownerId);

        Chat_Room room = Chat_Room.builder()
                .type(RoomType.GROUP)
                .name(name)
                .build();

        Chat_Room savedRoom = chatRoomRepository.save(room);

        List<Chat_Room_Member> members = unique.stream()
                .map(this::getUserOrThrow)
                .map(u -> Chat_Room_Member.builder().room(savedRoom).user(u).build())
                .toList();

        chatRoomMemberRepository.saveAll(members);

        return ChatRoomResponseDto.builder()
                .roomId(savedRoom.getId())
                .type(savedRoom.getType().name())
                .name(savedRoom.getName())
                .createdAt(savedRoom.getCreatedAt())
                .build();
    }

    //메시지 전송(저장 + 실시간 브로드 캐스트
    public ChatMessageBroadcastResponseDto sendMessage(Long roomId, Long senderUserId, ChatMessageSendRequestDto requestDto){
        // 채팅을 위한 기능
        // 내용 입력 검증(빈칸이면 안되고, 길이또한 제한되어야함)
     validateMessageContent(requestDto.getContent());

        // 방 조회
     Chat_Room room = chatRoomRepository.findById(roomId)
             .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 방입니다."));

     validateMember(roomId, senderUserId);



     //메시지 엔터티 생성 후 저장
        User sender = getUserOrThrow(senderUserId);

        Chat_Message saved = chatMessageRepository.save(
                Chat_Message.builder()
                        .room(room)
                        .user(sender)
                        .content(requestDto.getContent().trim())
                        .build()
        );

     // 응답 dto 생성
        ChatMessageBroadcastResponseDto response = ChatMessageBroadcastResponseDto.builder()
                .messageId(saved.getId())
                .roomId(room.getId())
                .senderId(sender.getId())
                .senderName(sender.getName())
                .content(saved.getContent())
                .createdAt(saved.getCreatedAt())
                .build();

        simpMessagingTemplate.convertAndSend("/subscribe/rooms/" + roomId, response);

        return response;
    }

 //메시지 히스토리 조회용(HTTP)
 @Transactional(readOnly = true)
    public ChatMessageListResponseDto getMessage(Long roomId, Long requesterUserId, int page, int size){
        // 재접속/새로고침 시 복원
        //멤버십 검증
     validateMember(roomId,requesterUserId);

     int safePage = Math.max(page, 0);
     int safeSize = Math.min(Math.max(size, 1), 50);
        //messageRepository로 페이징 조회
     Pageable pageable = PageRequest.of(safePage, safeSize, Sort.by(Sort.Direction.ASC, "createdAt"));

     Page<Chat_Message> result =
             chatMessageRepository.findByRoomIdOrderByCreatedAtAsc(roomId, pageable);
        //messages Dto 매핑
     List<ChatMessageBroadcastResponseDto> messages = result.getContent().stream()
             .map(m-> ChatMessageBroadcastResponseDto.builder()
                     .messageId(m.getId())
                     .roomId(m.getRoom().getId())
                     .senderId(m.getUser().getId())
                     .senderName(m.getUser().getName())
                     .content(m.getContent())
                     .createdAt(m.getCreatedAt())
                     .build())
             .toList();
     return ChatMessageListResponseDto.builder()
             .roomId(roomId)
             .message(messages)
             .build();
    }

    // 내 방 목록 조회
    @Transactional(readOnly = true)
    public List<ChatRoomListItemResponseDto> getMyRooms(Long  myUserId){
        // 내가 참여한 모든 채팅방 리스트 화면
        List<Chat_Room_Member> memberships = chatRoomMemberRepository.findByUserId_Id(myUserId);

        List<Long> roomIds= memberships.stream()
                .map(m-> m.getRoom().getId())
                .distinct()
                .toList();

        if(roomIds.isEmpty()) return List.of();

        // 방 정보 한 번에 조회
        List<Chat_Room> rooms = chatRoomRepository.findAllById(roomIds);
        Map<Long, Chat_Room> roomMap = rooms.stream()
                .collect(Collectors.toMap(Chat_Room::getId, r -> r));

        // room 정보 + 마지막 메시지 조회해서 List item dto 구성(제일 최근에 보낸 메시지를 제일 상단으로 올릴 수 있도록)
        List<ChatRoomListItemResponseDto> items = new ArrayList<>();
        for (Long roomId : roomIds) {
            Chat_Room room = roomMap.get(roomId);
            if (room == null) continue;

            Optional<Chat_Message> lastOpt = chatMessageRepository.findByRoomIdOrderByCreatedAtDesc(roomId);

            String lastMessage = lastOpt.map(Chat_Message::getContent).orElse(null);
            var lastAt = lastOpt.map(Chat_Message::getCreatedAt).orElse(null);

            items.add(ChatRoomListItemResponseDto.builder()
                    .roomId(room.getId())
                    .type(room.getType().name())
                    .name(room.getName())
                    .lastMessage(lastMessage)
                    .lastMessageAt(lastAt)
                    .build());
        }
        // 최신 대화 순 정렬(마지막 메시지 시간 기준)
        items.sort((a, b) -> {
            if (a.getLastMessageAt() == null && b.getLastMessageAt() == null) return 0;
            if (a.getLastMessageAt() == null) return 1;
            if (b.getLastMessageAt() == null) return -1;
            return b.getLastMessageAt().compareTo(a.getLastMessageAt());
        });

        return items;
    }

    // ----- 검증 및 조회를 위한 메서드 -----

    // null/blank 차단 및 길이 제한
    private void validateMessageContent(String content){
        //널과ㅏ 빈칸이면 차단하고 길이를 제한
     if(content == null){
      throw new IllegalArgumentException("메시지 내용이 없습니다.");
     }

     String trimmed = content.trim();
     if(trimmed.isEmpty()){
      throw new IllegalArgumentException("공백 메시지는 보낼 수 없습니다.");
     }

     if(trimmed.length()>2000){
      throw new IllegalArgumentException("메시지는 최대 2000자까지만 가능합니다.");
     }
    }

    // 멤버 검증
    private void validateMember(Long roomId, Long userId){
        boolean ok = chatRoomMemberRepository.existsByRoomIdAndUserId(roomId, userId);

        if(!ok){
         throw new IllegalArgumentException("해당 채팅방의 멤버가 아닙니다.");
        }
    }

    //중복으로 사용되는 채팅방 검증 및 사용자 검증 따로 빼두기
    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    }

    private Long getUserIdByEmailOrThrow(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("인증 정보(email)가 없습니다.");
        }
        User user = userRepository.findByEmail(email.trim())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다. email=" + email));
        return user.getId();
    }

}
