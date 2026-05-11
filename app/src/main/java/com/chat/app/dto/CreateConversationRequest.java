package com.chat.app.dto;

import java.util.List;

/**
 * Request payload for creating a new conversation thread.
 */
public record CreateConversationRequest(Long creatorId, String title, List<Long> memberIds) {
}
