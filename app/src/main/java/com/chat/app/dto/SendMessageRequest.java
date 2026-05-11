package com.chat.app.dto;

public record SendMessageRequest(Long conversationId, Long senderId, String content) {
}
