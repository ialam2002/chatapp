package com.chat.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chat.app.model.Contact;

/**
 * Data access operations for user contacts.
 */
public interface ContactRepository extends JpaRepository<Contact, Long> {
    boolean existsByOwnerIdAndContactUserId(Long ownerId, Long contactUserId);

    List<Contact> findByOwnerIdAndStatus(Long ownerId, String status);
}
