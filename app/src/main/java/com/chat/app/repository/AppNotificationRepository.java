package com.chat.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chat.app.model.AppNotification;

/**
 * Data access operations for user notifications.
 */
public interface AppNotificationRepository extends JpaRepository<AppNotification, Long> {
    List<AppNotification> findByUserIdOrderByCreatedAtDesc(Long userId);
}
