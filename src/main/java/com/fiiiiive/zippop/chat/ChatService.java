package com.fiiiiive.zippop.chat;

import com.fiiiiive.zippop.chat.chatmessage.ChatMessage;
import com.fiiiiive.zippop.chat.chatmessage.ChatMessageRepository;
import com.fiiiiive.zippop.chat.chatroom.ChatRoom;
import com.fiiiiive.zippop.chat.chatroom.ChatRoomRepository;
import com.fiiiiive.zippop.chat.model.request.ChatMessageReq;
import com.fiiiiive.zippop.chat.model.request.ChatRoomReq;
import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponse;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public BaseResponse<List<ChatRoom>> getChatRooms() throws BaseException {
        try {
            List<ChatRoom> chatRooms = chatRoomRepository.findAll();
            return new BaseResponse<>(BaseResponseMessage.CHAT_ROOM_SEARCH_SUCCESS, chatRooms);
        } catch (Exception e) {
            throw new BaseException(BaseResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public BaseResponse<ChatRoom> createChatRoom(ChatRoomReq chatRoomReq) throws BaseException {
        try {
            if (chatRoomRepository.findByName(chatRoomReq.getName()).isPresent()) {
                throw new BaseException(BaseResponseMessage.CHAT_ROOM_CREATE_FAIL);
            }
            ChatRoom chatRoom = ChatRoom.builder()
                    .name(chatRoomReq.getName())
                    .build();
            ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
            return new BaseResponse<>(BaseResponseMessage.CHAT_ROOM_CREATE_SUCCESS, savedChatRoom);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(BaseResponseMessage.CHAT_ROOM_CREATE_FAIL, e.getMessage());
        }
    }

    public BaseResponse<List<ChatMessage>> getMessages(String roomName) throws BaseException {
        try {
            long startTime = System.currentTimeMillis();
            ChatRoom chatRoom = chatRoomRepository.findByName(roomName)
                    .orElseThrow(() -> new BaseException(BaseResponseMessage.CHAT_ROOM_SEARCH_FAIL));

            List<ChatMessage> messages = chatRoom.getMessages();

            long endTime = System.currentTimeMillis();

            return new BaseResponse<>(BaseResponseMessage.CHAT_HISTORY_SEARCH_SUCCESS, messages);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(BaseResponseMessage.CHAT_HISTORY_SEARCH_FAIL, e.getMessage());
        }
    }

    public BaseResponse<List<ChatMessage>> getMessagesWithFetchJoin(String roomName) throws BaseException {
        try {
            long startTime = System.currentTimeMillis();

            ChatRoom chatRoom = chatRoomRepository.findByName(roomName)
                    .orElseThrow(() -> new BaseException(BaseResponseMessage.CHAT_ROOM_SEARCH_FAIL));
            List<ChatMessage> messages = chatMessageRepository.findByRoomNameFetchJoin(roomName);

            long endTime = System.currentTimeMillis();
            System.out.println("성능 개선 후 실행 시간: " + (endTime - startTime) + "ms");

            return new BaseResponse<>(BaseResponseMessage.CHAT_HISTORY_SEARCH_SUCCESS, messages);
        } catch (Exception e) {
            throw new BaseException(BaseResponseMessage.CHAT_HISTORY_SEARCH_FAIL, e.getMessage());
        }
    }

    public BaseResponse<ChatMessage> addMessage(ChatMessageReq chatMessageReq) throws BaseException {
        try {
            ChatRoom chatRoom = chatRoomRepository.findByName(chatMessageReq.getRoomName())
                    .orElseThrow(() -> new BaseException(BaseResponseMessage.CHAT_ROOM_SEARCH_FAIL));
            ChatMessage chatMessage = ChatMessage.builder()
                    .sender(chatMessageReq.getSender())
                    .content(chatMessageReq.getContent())
                    .chatRoom(chatRoom)
                    .build();

            ChatMessage savedMessage = chatMessageRepository.save(chatMessage);
            return new BaseResponse<>(BaseResponseMessage.CHAT_MESSAGE_SEND_SUCCESS, savedMessage);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(BaseResponseMessage.CHAT_MESSAGE_SEND_FAIL, e.getMessage());
        }
    }
}
