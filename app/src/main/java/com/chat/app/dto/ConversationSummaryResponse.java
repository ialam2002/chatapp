package com.chat.app.dto;

import java.time.Instant;

public record ConversationSummaryResponse(
        Long conversationId,
        String title,
        String lastMessage,
        Instant updatedAt,
        long unreadCount) {
}
