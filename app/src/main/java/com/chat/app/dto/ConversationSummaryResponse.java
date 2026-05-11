package com.chat.app.dto;

import java.time.Instant;

/**
 * Summary view used for recent chat listings.
 */
public record ConversationSummaryResponse(
        Long conversationId,
        String title,
        String lastMessage,
        Instant updatedAt,
        long unreadCount) {
}
