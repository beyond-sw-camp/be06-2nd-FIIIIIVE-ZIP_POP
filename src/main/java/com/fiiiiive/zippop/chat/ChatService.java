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
import com.fiiiiive.zippop.member.CustomerRepository;
import com.fiiiiive.zippop.member.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public BaseResponse<List<ChatRoom>> getChatRooms(String email) throws BaseException {
        try {
            Customer customer = customerRepository.findByCustomerEmail(email)
                    .orElseThrow(() -> new BaseException(BaseResponseMessage.USER_NOT_FOUND));
            List<ChatRoom> chatRooms = chatRoomRepository.findByCustomer(customer);
            return new BaseResponse<>(BaseResponseMessage.CHAT_ROOM_SEARCH_SUCCESS, chatRooms);
        } catch (Exception e) {
            throw new BaseException(BaseResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public BaseResponse<ChatRoom> createChatRoom(ChatRoomReq chatRoomReq, String email) throws BaseException {
        try {
            Customer customer = customerRepository.findByCustomerEmail(email)
                    .orElseThrow(() -> new BaseException(BaseResponseMessage.USER_NOT_FOUND));
            if (chatRoomRepository.findByName(chatRoomReq.getName()).isPresent()) {
                throw new BaseException(BaseResponseMessage.CHAT_ROOM_CREATE_FAIL);
            }
            ChatRoom chatRoom = ChatRoom.builder()
                    .name(chatRoomReq.getName())
                    .customer(customer)
                    .build();
            ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
            return new BaseResponse<>(BaseResponseMessage.CHAT_ROOM_CREATE_SUCCESS, savedChatRoom);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(BaseResponseMessage.CHAT_ROOM_CREATE_FAIL, e.getMessage());
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

    public BaseResponse<List<ChatMessage>> getMessagesWithFetchJoin(String roomName) throws BaseException {
        try {
            List<ChatMessage> messages = chatMessageRepository.findByRoomNameFetchJoin(roomName);
            return new BaseResponse<>(BaseResponseMessage.CHAT_HISTORY_SEARCH_SUCCESS, messages);
        } catch (Exception e) {
            throw new BaseException(BaseResponseMessage.CHAT_HISTORY_SEARCH_FAIL, e.getMessage());
        }
    }
}
