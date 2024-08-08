
package com.fiiiiive.zippop.chat;

import com.fiiiiive.zippop.chat.chatmessage.ChatMessage;
import com.fiiiiive.zippop.chat.model.request.ChatMessageReq;
import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponse;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class ChatController {

    @Autowired
    private ChatService chatService;
//    @Autowired
//    private ChatUserSessionService chatUserSessionService;

    @MessageMapping("/chat.sendMessage/{roomId}")
    @SendTo("/topic/{roomId}")
    public ChatMessage sendMessage(@DestinationVariable String roomId, @Payload ChatMessage chatMessage) throws BaseException {
        log.info("[SENDER - {}] messages : {}", chatMessage.getSender(), chatMessage.getContent());

        ChatMessageReq chatMessageReq = new ChatMessageReq();
        chatMessageReq.setSender(chatMessage.getSender());
        chatMessageReq.setContent(chatMessage.getContent());
        chatMessageReq.setRoomName(roomId);

        BaseResponse<ChatMessage> response = chatService.addMessage(chatMessageReq);

        if (response.getSuccess()) {
            return response.getResult();
        } else {
            throw new BaseException(BaseResponseMessage.CHAT_MESSAGE_SEND_FAIL);
        }
    }

    @MessageMapping("/chat.addUser/{roomId}")
    @SendTo("/topic/{roomId}")
    public ChatMessage addUser(@DestinationVariable String roomId, @Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        log.info("[ADDUSER - {}] ", chatMessage.getSender());
        return chatMessage;
    }
}

