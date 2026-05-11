package com.chat.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chat.app.model.Conversation;

/**
 * Data access operations for conversations.
 */
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
}
