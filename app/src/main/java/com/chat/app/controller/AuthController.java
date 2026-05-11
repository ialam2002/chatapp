package com.chat.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.chat.app.dto.AuthRequest;
import com.chat.app.dto.UserResponse;
import com.chat.app.model.AppUser;
import com.chat.app.service.ChatAppService;

/**
 * Handles basic sign-up and login operations.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final ChatAppService chatAppService;

    public AuthController(ChatAppService chatAppService) {
        this.chatAppService = chatAppService;
    }

    /**
     * Creates a new user account.
     *
     * @param request username and password payload
     * @return created user summary
     */
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse signup(@RequestBody AuthRequest request) {
        AppUser user = chatAppService.signup(request.username(), request.password());
        return new UserResponse(user.getId(), user.getUsername());
    }

    /**
     * Authenticates an existing user.
     *
     * @param request username and password payload
     * @return authenticated user summary
     */
    @PostMapping("/login")
    public UserResponse login(@RequestBody AuthRequest request) {
        AppUser user = chatAppService.login(request.username(), request.password());
        return new UserResponse(user.getId(), user.getUsername());
    }
}
