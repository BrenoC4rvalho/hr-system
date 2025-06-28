package com.example.backend.controller;

import com.example.backend.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> generate(@RequestParam String message, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return Flux.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Valid authentication token is required."));
        }
        return chatService.generateResponse(message);
    }


}
