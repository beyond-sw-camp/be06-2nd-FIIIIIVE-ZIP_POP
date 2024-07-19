//package com.fiiiiive.zippop.chat.chatuser;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.event.EventListener;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Service;
//import org.springframework.web.socket.messaging.SessionConnectedEvent;
//import org.springframework.web.socket.messaging.SessionDisconnectEvent;
//
//import java.util.concurrent.atomic.AtomicInteger;
//
//@Service
//public class ChatUserSessionService {
//
//    private final AtomicInteger userCount = new AtomicInteger(0);
//    private final SimpMessagingTemplate messagingTemplate;
//
//    @Autowired
//    public ChatUserSessionService(SimpMessagingTemplate messagingTemplate) {
//        this.messagingTemplate = messagingTemplate;
//    }
//
//    @EventListener
//    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
//        int currentCount = userCount.incrementAndGet();
//        broadcastUserCount(currentCount);
//    }
//
//    @EventListener
//    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
//        int currentCount = userCount.decrementAndGet();
//        broadcastUserCount(currentCount);
//    }
//
//    public int getUserCount() {
//        return userCount.get();
//    }
//
//    private void broadcastUserCount(int count) {
//        messagingTemplate.convertAndSend("/topic/userCount", count);
//    }
//}
