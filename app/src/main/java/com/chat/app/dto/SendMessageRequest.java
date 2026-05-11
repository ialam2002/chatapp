package com.chat.app.dto;

/**
 * HTTP request payload for sending a message.
 */
public record SendMessageRequest(Long conversationId, Long senderId, String content) {
}
