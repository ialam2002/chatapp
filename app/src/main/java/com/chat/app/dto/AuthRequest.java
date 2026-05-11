package com.chat.app.dto;

/**
 * Request payload for sign-up and login operations.
 */
public record AuthRequest(String username, String password) {
}
