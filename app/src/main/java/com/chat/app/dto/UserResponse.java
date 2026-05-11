package com.chat.app.dto;

/**
 * User projection returned to clients.
 */
public record UserResponse(Long id, String username) {
}
