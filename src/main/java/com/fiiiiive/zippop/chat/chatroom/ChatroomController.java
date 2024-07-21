package com.fiiiiive.zippop.chat.chatroom;

import com.fiiiiive.zippop.chat.ChatService;
import com.fiiiiive.zippop.chat.chatmessage.ChatMessage;
import com.fiiiiive.zippop.chat.model.request.ChatMessageReq;
import com.fiiiiive.zippop.chat.model.request.ChatRoomReq;
import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponse;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import com.fiiiiive.zippop.utils.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@Tag(name = "chat-api", description = "Chat")
@Slf4j
@RestController
@RequestMapping("/api/v1/chat")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ChatroomController {

    private final JwtUtil jwtUtil;
    private final ChatService chatService;

    @Autowired
    public ChatroomController(JwtUtil jwtUtil, ChatService chatService) {
        this.jwtUtil = jwtUtil;
        this.chatService = chatService;
    }

    @PostMapping("/rooms")
    public BaseResponse<ChatRoom> createRoom(@RequestBody ChatRoomReq dto) throws BaseException {
        return chatService.createChatRoom(dto);
    }
    @PostMapping("/rooms/verify")
    public ResponseEntity<?> createRoom(@RequestBody ChatRoomReq dto, @RequestHeader("Authorization") String token) throws BaseException {
        String token0 = token.split(" ")[1];
        String role = jwtUtil.getRole(token0);
        if ("ROLE_COMPANY".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("기업회원은 채팅방을 생성할 수 없습니다.");
        }
        BaseResponse<ChatRoom> response = chatService.createChatRoom(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/rooms")
    public BaseResponse<List<ChatRoom>> getChatRooms() throws BaseException {
        return chatService.getChatRooms();
    }



    @PostMapping("/rooms/{roomName}/messages")
    public BaseResponse<ChatMessage> addMessage(@PathVariable String roomName, @RequestBody ChatMessageReq dto) throws BaseException {
        dto.setRoomName(roomName);
        return chatService.addMessage(dto);
    }
    //    @GetMapping("/rooms/{roomName}/messages")
//    public BaseResponse<List<ChatMessage>> getMessages(@PathVariable String roomName) throws BaseException {
//        return chatService.getMessages(roomName);
//    } //성능개선 전
    @GetMapping("/rooms/{roomName}/messages")
    public BaseResponse<List<ChatMessage>> getMessagesWithFetchJoin(@PathVariable String roomName) throws BaseException {
        return chatService.getMessagesWithFetchJoin(roomName);
    }

}
