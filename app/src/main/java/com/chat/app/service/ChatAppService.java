package com.chat.app.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chat.app.dto.ConversationSummaryResponse;
import com.chat.app.dto.MessageResponse;
import com.chat.app.dto.NotificationResponse;
import com.chat.app.model.AppNotification;
import com.chat.app.model.AppUser;
import com.chat.app.model.Contact;
import com.chat.app.model.Conversation;
import com.chat.app.model.ConversationMember;
import com.chat.app.model.MessageEntity;
import com.chat.app.repository.AppNotificationRepository;
import com.chat.app.repository.AppUserRepository;
import com.chat.app.repository.ContactRepository;
import com.chat.app.repository.ConversationMemberRepository;
import com.chat.app.repository.ConversationRepository;
import com.chat.app.repository.MessageEntityRepository;

/**
 * Contains the main business logic for users, contacts, conversations,
 * messages, and notifications.
 */
@Service
@Transactional
public class ChatAppService {
    private final AppUserRepository appUserRepository;
    private final ContactRepository contactRepository;
    private final ConversationRepository conversationRepository;
    private final ConversationMemberRepository conversationMemberRepository;
    private final MessageEntityRepository messageEntityRepository;
    private final AppNotificationRepository appNotificationRepository;

    public ChatAppService(
            AppUserRepository appUserRepository,
            ContactRepository contactRepository,
            ConversationRepository conversationRepository,
            ConversationMemberRepository conversationMemberRepository,
            MessageEntityRepository messageEntityRepository,
            AppNotificationRepository appNotificationRepository) {
        this.appUserRepository = appUserRepository;
        this.contactRepository = contactRepository;
        this.conversationRepository = conversationRepository;
        this.conversationMemberRepository = conversationMemberRepository;
        this.messageEntityRepository = messageEntityRepository;
        this.appNotificationRepository = appNotificationRepository;
    }

    /**
     * Registers a new user after basic validation.
     *
     * @param username username to register
     * @param password raw password
     * @return persisted user
     */
    public AppUser signup(String username, String password) {
        String normalized = username == null ? "" : username.trim();
        if (normalized.isEmpty() || password == null || password.isBlank()) {
            throw new IllegalArgumentException("Username and password are required.");
        }
        if (appUserRepository.findByUsername(normalized).isPresent()) {
            throw new IllegalArgumentException("Username already exists.");
        }
        AppUser user = new AppUser();
        user.setUsername(normalized);
        user.setPassword(password);
        user.setCreatedAt(Instant.now());
        return appUserRepository.save(user);
    }

    /**
     * Authenticates a user by matching username and password.
     *
     * @param username username credential
     * @param password password credential
     * @return authenticated user
     */
    public AppUser login(String username, String password) {
        AppUser user = appUserRepository.findByUsername(username == null ? "" : username.trim())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials."));
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid credentials.");
        }
        return user;
    }

    /**
     * Lists all registered users.
     *
     * @return all users
     */
    public List<AppUser> listUsers() {
        return appUserRepository.findAll();
    }

    /**
     * Creates an accepted contact relationship in both directions.
     *
     * @param ownerId owner user id
     * @param contactUsername username to add as contact
     */
    public void addContact(Long ownerId, String contactUsername) {
        AppUser owner = getUser(ownerId);
        AppUser contactUser = appUserRepository.findByUsername(contactUsername == null ? "" : contactUsername.trim())
                .orElseThrow(() -> new NoSuchElementException("Contact user not found."));

        if (owner.getId().equals(contactUser.getId())) {
            throw new IllegalArgumentException("You cannot add yourself as a contact.");
        }

        if (!contactRepository.existsByOwnerIdAndContactUserId(owner.getId(), contactUser.getId())) {
            Contact c1 = new Contact();
            c1.setOwner(owner);
            c1.setContactUser(contactUser);
            c1.setStatus("ACCEPTED");
            contactRepository.save(c1);
        }

        if (!contactRepository.existsByOwnerIdAndContactUserId(contactUser.getId(), owner.getId())) {
            Contact c2 = new Contact();
            c2.setOwner(contactUser);
            c2.setContactUser(owner);
            c2.setStatus("ACCEPTED");
            contactRepository.save(c2);
        }
    }

    /**
     * Returns accepted contacts for a user.
     *
     * @param ownerId owner user id
     * @return contact users
     */
    public List<AppUser> listContacts(Long ownerId) {
        return contactRepository.findByOwnerIdAndStatus(ownerId, "ACCEPTED")
                .stream()
                .map(Contact::getContactUser)
                .toList();
    }

    /**
     * Creates a conversation and adds creator plus selected members.
     *
     * @param creatorId user creating the conversation
     * @param title conversation display title
     * @param memberIds additional user ids to add
     * @return persisted conversation
     */
    public Conversation createConversation(Long creatorId, String title, List<Long> memberIds) {
        AppUser creator = getUser(creatorId);

        Conversation conversation = new Conversation();
        conversation.setTitle((title == null || title.isBlank()) ? "Untitled Chat" : title.trim());
        conversation.setUpdatedAt(Instant.now());
        conversation = conversationRepository.save(conversation);

        Set<Long> uniqueMemberIds = new HashSet<>();
        uniqueMemberIds.add(creator.getId());
        if (memberIds != null) {
            uniqueMemberIds.addAll(memberIds);
        }

        for (Long memberId : uniqueMemberIds) {
            AppUser user = getUser(memberId);
            ConversationMember member = new ConversationMember();
            member.setConversation(conversation);
            member.setUser(user);
            conversationMemberRepository.save(member);
        }

        return conversation;
    }

    /**
     * Persists a message and generates notifications for other members.
     *
     * @param conversationId target conversation
     * @param senderId sender user id
     * @param content message text
     * @return saved message projection
     */
    public MessageResponse sendMessage(Long conversationId, Long senderId, String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Message content cannot be empty.");
        }

        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new NoSuchElementException("Conversation not found."));

        if (!conversationMemberRepository.existsByConversationIdAndUserId(conversationId, senderId)) {
            throw new IllegalArgumentException("Sender is not part of this conversation.");
        }

        AppUser sender = getUser(senderId);

        MessageEntity message = new MessageEntity();
        message.setConversation(conversation);
        message.setSender(sender);
        message.setContent(content.trim());
        message.setSentAt(Instant.now());
        message = messageEntityRepository.save(message);

        conversation.setUpdatedAt(message.getSentAt());
        conversationRepository.save(conversation);

        List<ConversationMember> members = conversationMemberRepository.findByConversationId(conversationId);
        for (ConversationMember member : members) {
            if (!member.getUser().getId().equals(senderId)) {
                AppNotification notification = new AppNotification();
                notification.setUser(member.getUser());
                notification.setContent(sender.getUsername() + " sent a message in " + conversation.getTitle());
                notification.setReadFlag(false);
                notification.setCreatedAt(Instant.now());
                appNotificationRepository.save(notification);
            }
        }

        return toMessageResponse(message);
    }

    /**
     * Loads message history for a conversation after membership validation.
     *
     * @param conversationId target conversation
     * @param userId requesting user
     * @return chronologically ordered messages
     */
    public List<MessageResponse> getConversationMessages(Long conversationId, Long userId) {
        if (!conversationMemberRepository.existsByConversationIdAndUserId(conversationId, userId)) {
            throw new IllegalArgumentException("You are not part of this conversation.");
        }

        return messageEntityRepository.findByConversationIdOrderBySentAtAsc(conversationId)
                .stream()
                .map(this::toMessageResponse)
                .toList();
    }

    /**
     * Returns chat summaries (last message, updated time, unread count)
     * for all conversations of the given user.
     *
     * @param userId target user id
     * @return recent chat summaries
     */
    public List<ConversationSummaryResponse> getRecentChats(Long userId) {
        List<ConversationMember> memberships = conversationMemberRepository.findByUserId(userId);
        List<ConversationSummaryResponse> response = new ArrayList<>();

        for (ConversationMember membership : memberships) {
            Conversation conversation = membership.getConversation();
            String lastMessage = messageEntityRepository.findTopByConversationIdOrderBySentAtDesc(conversation.getId())
                    .map(MessageEntity::getContent)
                    .orElse("No messages yet");

            long unreadCount = appNotificationRepository.findByUserIdOrderByCreatedAtDesc(userId)
                    .stream()
                    .filter(n -> !n.isReadFlag() && n.getContent().contains(conversation.getTitle()))
                    .count();

            response.add(new ConversationSummaryResponse(
                    conversation.getId(),
                    conversation.getTitle(),
                    lastMessage,
                    conversation.getUpdatedAt(),
                    unreadCount));
        }

        response.sort((a, b) -> b.updatedAt().compareTo(a.updatedAt()));
        return response;
    }

    /**
     * Returns all notifications for a user.
     *
     * @param userId target user id
     * @return notification projections
     */
    public List<NotificationResponse> getNotifications(Long userId) {
        return appNotificationRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(n -> new NotificationResponse(n.getId(), n.getContent(), n.isReadFlag(), n.getCreatedAt()))
                .toList();
    }

    /**
     * Marks a specific notification as read for the owner.
     *
     * @param notificationId notification id
     * @param userId owner user id
     */
    public void markNotificationAsRead(Long notificationId, Long userId) {
        AppNotification notification = appNotificationRepository.findById(notificationId)
                .orElseThrow(() -> new NoSuchElementException("Notification not found."));

        if (!notification.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Notification does not belong to user.");
        }

        notification.setReadFlag(true);
        appNotificationRepository.save(notification);
    }

    private AppUser getUser(Long userId) {
        return appUserRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found."));
    }

    private MessageResponse toMessageResponse(MessageEntity message) {
        return new MessageResponse(
                message.getId(),
                message.getConversation().getId(),
                message.getSender().getId(),
                message.getSender().getUsername(),
                message.getContent(),
                message.getSentAt());
    }
}
