package com.chat.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Serves the main chat UI template.
 */
@Controller
public class ChatController {
    /**
     * Returns the chat page view.
     *
     * @return thymeleaf template name
     */
    @GetMapping
    public String chat() {
        return "chat";
    }
}
