package com.chat.app.dto;

public record WsMessageRequest(Long conversationId, Long senderId, String content) {
}
