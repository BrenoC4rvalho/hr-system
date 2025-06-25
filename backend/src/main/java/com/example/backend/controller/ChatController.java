package com.example.backend.controller;

import com.example.backend.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(
        ChatService chatService
    ) {
        this.chatService = chatService;
    }

    @PostMapping
    public Flux<String> generate(@RequestBody String message, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return Flux.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Valid authentication token is required."));
        }
        return chatService.generateResponse(message);
    }


}
