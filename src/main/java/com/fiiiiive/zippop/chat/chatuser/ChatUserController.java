//package com.fiiiiive.zippop.chat.chatuser;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api")
//public class ChatUserController {
//
//    private final ChatUserSessionService chatuserSessionService;
//
//    @Autowired
//    public ChatUserController(ChatUserSessionService chatuserSessionService) {
//        this.chatuserSessionService = chatuserSessionService;
//    }
//
//    @GetMapping("/userCount")
//    public int getUserCount() {
//        return chatuserSessionService.getUserCount();
//    }
//}