package com.fiiiiive.zippop.chat;

import com.fiiiiive.zippop.chat.chatmessage.ChatMessage;
import com.fiiiiive.zippop.chat.chatmessage.ChatMessageRepository;
import com.fiiiiive.zippop.chat.chatroom.ChatRoom;
import com.fiiiiive.zippop.chat.chatroom.ChatRoomDto;
import com.fiiiiive.zippop.chat.chatroom.ChatRoomRepository;
import com.fiiiiive.zippop.chat.model.request.ChatMessageReq;
import com.fiiiiive.zippop.chat.model.request.ChatRoomReq;
import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponse;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import com.fiiiiive.zippop.member.CompanyRepository;
import com.fiiiiive.zippop.member.CustomerRepository;
import com.fiiiiive.zippop.member.model.Company;
import com.fiiiiive.zippop.member.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ChatService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;


    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CompanyRepository companyRepository;


    public BaseResponse<List<ChatRoomDto>> getChatRooms(String email, String role) throws BaseException {
        try {
            List<ChatRoom> chatRooms;
            if ("ROLE_CUSTOMER".equals(role)) {
                Customer customer = customerRepository.findByCustomerEmail(email)
                        .orElseThrow(() -> new BaseException(BaseResponseMessage.USER_NOT_FOUND));
                chatRooms = chatRoomRepository.findByCustomer(customer);
            } else if ("ROLE_COMPANY".equals(role)) {
                Company company = companyRepository.findByCompanyEmail(email)
                        .orElseThrow(() -> new BaseException(BaseResponseMessage.USER_NOT_FOUND));
                chatRooms = chatRoomRepository.findByCompany(company);
            } else {
                throw new BaseException(BaseResponseMessage.USER_NOT_FOUND);
            }
            List<ChatRoomDto> chatRoomDtos = chatRooms.stream()
                    .map(room -> new ChatRoomDto(room.getId(), room.getName()))
                    .toList();
            return new BaseResponse<>(BaseResponseMessage.CHAT_ROOM_SEARCH_SUCCESS, chatRoomDtos);
        } catch (Exception e) {
            throw new BaseException(BaseResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }



    public BaseResponse<ChatRoomDto> createChatRoom(ChatRoomReq chatRoomReq, String customerEmail, String companyEmail) throws BaseException {
        try {
            Customer customer = customerRepository.findByCustomerEmail(customerEmail)
                    .orElseThrow(() -> new BaseException(BaseResponseMessage.USER_NOT_FOUND));
            Company company = companyRepository.findByCompanyEmail(companyEmail)
                    .orElseThrow(() -> new BaseException(BaseResponseMessage.USER_NOT_FOUND));

            if (chatRoomRepository.findByCustomerAndCompany(customer, company).isPresent()) {
                throw new BaseException(BaseResponseMessage.CHAT_ROOM_CREATE_FAIL);
            }

            ChatRoom chatRoom = ChatRoom.builder()
                    .name(chatRoomReq.getName())
                    .customer(customer)
                    .company(company)
                    .build();

            ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
            ChatRoomDto chatRoomDto = new ChatRoomDto(savedChatRoom.getId(), savedChatRoom.getName());
            return new BaseResponse<>(BaseResponseMessage.CHAT_ROOM_CREATE_SUCCESS, chatRoomDto);
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


