package com.fiiiiive.zippop.chat.chatmessage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("SELECT cm FROM ChatMessage cm JOIN FETCH cm.chatRoom WHERE cm.chatRoom.name = :roomName")
    List<ChatMessage> findByRoomNameFetchJoin(@Param("roomName") String roomName);
}
