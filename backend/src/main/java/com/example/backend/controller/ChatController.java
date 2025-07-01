package com.example.backend.controller;

import com.example.backend.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    private final Map<String, Sinks.One<String>> responseSinks = new ConcurrentHashMap<>();

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

        String sessionId = authentication.getName() + ":" + System.currentTimeMillis();
        Sinks.One<String> fullResponseSink = Sinks.one();
        responseSinks.put(sessionId, fullResponseSink);

        return chatService.generateResponse(message, fullResponseSink)
                .doFinally(signalType -> {
                    Mono.delay(Duration.ofMinutes(5)).subscribe(t -> responseSinks.remove(sessionId));
                })
                .startWith("sessionId:" + sessionId);
    }

    @GetMapping("/full/{sessionId}")
    public Mono<ResponseEntity<Map<String, String>>> getFullResponse(@PathVariable String sessionId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }

        Sinks.One<String> sink = responseSinks.get(sessionId);
        if (sink == null) {
            return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Session not found or expired.")));
        }

        return sink.asMono()
                .map(response -> ResponseEntity.ok(Map.of("response", response)))
                .timeout(Duration.ofSeconds(60), Mono.just(ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build()));
    }


}
