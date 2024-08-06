package com.fiiiiive.zippop.chat.chatroom;

import com.fiiiiive.zippop.chat.ChatService;
import com.fiiiiive.zippop.chat.chatmessage.ChatMessage;
import com.fiiiiive.zippop.chat.model.request.ChatMessageReq;
import com.fiiiiive.zippop.chat.model.request.ChatRoomReq;
import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponse;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import com.fiiiiive.zippop.member.model.CustomUserDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "chat-api", description = "Chat")
@Slf4j
@RestController
@RequestMapping("/api/v1/chat")
@CrossOrigin(originPatterns = "*", allowedHeaders = "*",allowCredentials = "true")
public class ChatroomController {

    private final ChatService chatService;

    @Autowired
    public ChatroomController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/rooms")
    public BaseResponse<ChatRoom> createRoom(@RequestBody ChatRoomReq dto, @AuthenticationPrincipal CustomUserDetails customUserDetails) throws BaseException {
        String email = customUserDetails.getEmail();

        return chatService.createChatRoom(dto, email);
    }

    @PostMapping("/rooms/verify")
    public ResponseEntity<?> createRoomWithVerification(@RequestBody ChatRoomReq dto, @AuthenticationPrincipal CustomUserDetails customUserDetails) throws BaseException {
        String role = customUserDetails.getRole();
        if ("ROLE_COMPANY".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("기업회원은 채팅방을 생성할 수 없습니다.");
        }
        String email = customUserDetails.getEmail();
        BaseResponse<ChatRoom> response = chatService.createChatRoom(dto, email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/rooms")
    public BaseResponse<List<ChatRoom>> getChatRooms(@AuthenticationPrincipal CustomUserDetails customUserDetails) throws BaseException {
        String email = customUserDetails.getEmail();
        return chatService.getChatRooms(email);
    }

    @PostMapping("/rooms/{roomName}/messages")
    public BaseResponse<ChatMessage> addMessage(@PathVariable String roomName, @RequestBody ChatMessageReq dto, @AuthenticationPrincipal CustomUserDetails customUserDetails) throws BaseException {
        dto.setRoomName(roomName);
        dto.setSender(customUserDetails.getUsername());
        return chatService.addMessage(dto);
    }

    @GetMapping("/rooms/{roomName}/messages")
    public BaseResponse<List<ChatMessage>> getMessagesWithFetchJoin(@PathVariable String roomName) throws BaseException {
        return chatService.getMessagesWithFetchJoin(roomName);
    }

    @GetMapping("/user")
    public BaseResponse<String> getUsername(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return new BaseResponse<>(BaseResponseMessage.CHAT_USER_FOUND, customUserDetails.getUsername());
    }
}
