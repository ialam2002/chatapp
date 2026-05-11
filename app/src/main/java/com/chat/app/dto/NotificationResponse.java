package com.chat.app.dto;

import java.time.Instant;

public record NotificationResponse(Long id, String content, boolean readFlag, Instant createdAt) {
}
