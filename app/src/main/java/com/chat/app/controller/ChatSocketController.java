package com.chat.app.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.chat.app.dto.MessageResponse;
import com.chat.app.dto.WsMessageRequest;
import com.chat.app.service.ChatAppService;

@Controller
public class ChatSocketController {
    private final ChatAppService chatAppService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatSocketController(ChatAppService chatAppService, SimpMessagingTemplate messagingTemplate) {
        this.chatAppService = chatAppService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.send")
    public void sendMessage(WsMessageRequest request) {
        MessageResponse saved = chatAppService.sendMessage(request.conversationId(), request.senderId(), request.content());
        messagingTemplate.convertAndSend("/topic/conversations/" + saved.conversationId(), saved);
    }
}
