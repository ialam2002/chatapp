package com.chat.app.dto;

/**
 * Request payload for adding a contact.
 */
public record ContactRequest(Long ownerId, String contactUsername) {
}
