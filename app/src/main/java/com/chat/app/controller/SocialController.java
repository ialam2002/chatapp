package com.chat.app.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chat.app.dto.ContactRequest;
import com.chat.app.dto.UserResponse;
import com.chat.app.service.ChatAppService;

@RestController
@RequestMapping("/api")
public class SocialController {
    private final ChatAppService chatAppService;

    public SocialController(ChatAppService chatAppService) {
        this.chatAppService = chatAppService;
    }

    @PostMapping("/contacts/add")
    public void addContact(@RequestBody ContactRequest request) {
        chatAppService.addContact(request.ownerId(), request.contactUsername());
    }

    @GetMapping("/users/{userId}/contacts")
    public List<UserResponse> listContacts(@PathVariable Long userId) {
        return chatAppService.listContacts(userId)
                .stream()
                .map(u -> new UserResponse(u.getId(), u.getUsername()))
                .toList();
    }

    @GetMapping("/users")
    public List<UserResponse> listUsers() {
        return chatAppService.listUsers().stream().map(u -> new UserResponse(u.getId(), u.getUsername())).toList();
    }
}
