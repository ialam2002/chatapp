package com.chat.app.dto;

import java.time.Instant;

/**
 * Message projection returned to clients.
 */
public record MessageResponse(
        Long id,
        Long conversationId,
        Long senderId,
        String senderUsername,
        String content,
        Instant sentAt) {
}
