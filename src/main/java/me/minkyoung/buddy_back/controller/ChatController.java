package me.minkyoung.buddy_back.controller;

import lombok.RequiredArgsConstructor;
import me.minkyoung.buddy_back.dto.ChatMessageSendRequestDto;
import me.minkyoung.buddy_back.service.ChatService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController //Rest API 요청 컨트롤러
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/rooms/{roomId}/messages")
    public void sendMessage(@DestinationVariable Long roomId,
                            @Payload ChatMessageSendRequestDto req,
                            Principal principal) {

        String senderEmail = principal.getName();
        chatService.sendMessageByEmail(roomId, senderEmail, req);
    }

}
