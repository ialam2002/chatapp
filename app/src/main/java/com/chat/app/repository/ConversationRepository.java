package com.chat.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chat.app.model.Conversation;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
}
