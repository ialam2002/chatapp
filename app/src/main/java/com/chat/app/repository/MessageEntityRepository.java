package com.chat.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chat.app.model.MessageEntity;

public interface MessageEntityRepository extends JpaRepository<MessageEntity, Long> {
    List<MessageEntity> findByConversationIdOrderBySentAtAsc(Long conversationId);

    Optional<MessageEntity> findTopByConversationIdOrderBySentAtDesc(Long conversationId);
}
