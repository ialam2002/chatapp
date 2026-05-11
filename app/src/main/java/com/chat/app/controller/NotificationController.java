package com.chat.app.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chat.app.dto.NotificationResponse;
import com.chat.app.service.ChatAppService;

@RestController
@RequestMapping("/api")
public class NotificationController {
    private final ChatAppService chatAppService;

    public NotificationController(ChatAppService chatAppService) {
        this.chatAppService = chatAppService;
    }

    @GetMapping("/users/{userId}/notifications")
    public List<NotificationResponse> notifications(@PathVariable Long userId) {
        return chatAppService.getNotifications(userId);
    }

    @PostMapping("/notifications/{notificationId}/read")
    public void markAsRead(@PathVariable Long notificationId, @RequestParam Long userId) {
        chatAppService.markNotificationAsRead(notificationId, userId);
    }
}
