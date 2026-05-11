package com.chat.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chat.app.model.ConversationMember;

/**
 * Data access operations for conversation membership records.
 */
public interface ConversationMemberRepository extends JpaRepository<ConversationMember, Long> {
    boolean existsByConversationIdAndUserId(Long conversationId, Long userId);

    List<ConversationMember> findByConversationId(Long conversationId);

    List<ConversationMember> findByUserId(Long userId);
}
