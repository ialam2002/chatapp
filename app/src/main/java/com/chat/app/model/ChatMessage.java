package com.chat.app.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Legacy in-memory chat message model kept for compatibility.
 */
@Data
@NoArgsConstructor
public class ChatMessage {
private Long id;
private String sender;
private String content;
}
