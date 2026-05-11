package com.chat.app.dto;

/**
 * WebSocket payload for publishing a conversation message.
 */
public record WsMessageRequest(Long conversationId, Long senderId, String content) {
}
