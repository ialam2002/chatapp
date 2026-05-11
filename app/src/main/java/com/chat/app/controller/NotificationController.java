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

/**
 * Exposes endpoints for notification retrieval and read-state updates.
 */
@RestController
@RequestMapping("/api")
public class NotificationController {
    private final ChatAppService chatAppService;

    public NotificationController(ChatAppService chatAppService) {
        this.chatAppService = chatAppService;
    }

    /**
     * Returns notifications for a user in descending creation order.
     *
     * @param userId notification owner id
     * @return notification list
     */
    @GetMapping("/users/{userId}/notifications")
    public List<NotificationResponse> notifications(@PathVariable Long userId) {
        return chatAppService.getNotifications(userId);
    }

    /**
     * Marks a single notification as read.
     *
     * @param notificationId notification id
     * @param userId owner user id
     */
    @PostMapping("/notifications/{notificationId}/read")
    public void markAsRead(@PathVariable Long notificationId, @RequestParam Long userId) {
        chatAppService.markNotificationAsRead(notificationId, userId);
    }
}
