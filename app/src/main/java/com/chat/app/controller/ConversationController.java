package com.chat.app.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chat.app.dto.ConversationSummaryResponse;
import com.chat.app.dto.CreateConversationRequest;
import com.chat.app.dto.MessageResponse;
import com.chat.app.dto.SendMessageRequest;
import com.chat.app.model.Conversation;
import com.chat.app.service.ChatAppService;

/**
 * Exposes conversation and message REST endpoints.
 */
@RestController
@RequestMapping("/api")
public class ConversationController {
    private final ChatAppService chatAppService;

    public ConversationController(ChatAppService chatAppService) {
        this.chatAppService = chatAppService;
    }

    /**
     * Creates a new conversation with selected members.
     *
     * @param request conversation creation payload
     * @return created conversation entity
     */
    @PostMapping("/conversations/create")
    public Conversation createConversation(@RequestBody CreateConversationRequest request) {
        return chatAppService.createConversation(request.creatorId(), request.title(), request.memberIds());
    }

    /**
     * Returns recent chats for a user sorted by latest activity.
     *
     * @param userId requesting user id
     * @return list of conversation summaries
     */
    @GetMapping("/users/{userId}/recent-chats")
    public List<ConversationSummaryResponse> recentChats(@PathVariable Long userId) {
        return chatAppService.getRecentChats(userId);
    }

    /**
     * Returns full message history for a conversation.
     *
     * @param conversationId target conversation id
     * @param userId user requesting history
     * @return ordered message list
     */
    @GetMapping("/conversations/{conversationId}/messages")
    public List<MessageResponse> conversationMessages(@PathVariable Long conversationId, @RequestParam Long userId) {
        return chatAppService.getConversationMessages(conversationId, userId);
    }

    /**
     * Sends and persists a message over HTTP.
     *
     * @param request message payload
     * @return saved message projection
     */
    @PostMapping("/messages/send")
    public MessageResponse sendMessage(@RequestBody SendMessageRequest request) {
        return chatAppService.sendMessage(request.conversationId(), request.senderId(), request.content());
    }
}
