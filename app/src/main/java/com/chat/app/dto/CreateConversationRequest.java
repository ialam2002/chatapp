package com.chat.app.dto;

import java.util.List;

public record CreateConversationRequest(Long creatorId, String title, List<Long> memberIds) {
}
