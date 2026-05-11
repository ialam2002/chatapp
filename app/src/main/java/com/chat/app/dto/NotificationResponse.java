package com.chat.app.dto;

import java.time.Instant;

/**
 * Notification projection returned to clients.
 */
public record NotificationResponse(Long id, String content, boolean readFlag, Instant createdAt) {
}
