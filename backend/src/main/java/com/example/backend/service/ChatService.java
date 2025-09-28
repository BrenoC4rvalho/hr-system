package com.example.backend.service;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final ChatModel chatModel;
    private final VectorStore employeeVectorStore;
    private final VectorStore positionVectorStore;
    private final VectorStore departmentVectorStore;

    @Value("classpath:prompts/hr-assistant-chat-prompt.st")
    private Resource systemPromptResource;

    private String systemPromptTemplate;

    public ChatService(
            ChatModel chatModel,
            @Qualifier("employeeVectorStore") VectorStore employeeVectorStore,
            @Qualifier("positionVectorStore") VectorStore positionVectorStore,
            @Qualifier("departmentVectorStore") VectorStore departmentVectorStore
    ) {
        this.chatModel = chatModel;
        this.employeeVectorStore = employeeVectorStore;
        this.positionVectorStore = positionVectorStore;
        this.departmentVectorStore = departmentVectorStore;
    }

    @PostConstruct
    public void init() {
        try {
            this.systemPromptTemplate = systemPromptResource.getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load system prompt file.", e);
        }
    }

    public Flux<String> generateResponse(String userMessage, Sinks.One<String> fullResponseSink) {

        Prompt prompt = createPrompt(userMessage);

        StringBuilder responseBuilder = new StringBuilder();

        return chatModel.stream(prompt)
                .map(chatResponse -> {
                    String chunk = chatResponse.getResults().getFirst().getOutput().getText();
                    responseBuilder.append(chunk);
                    return chunk;
                })
                .doOnComplete(() -> {
                    fullResponseSink.tryEmitValue(responseBuilder.toString());
                });
    }

    private Prompt createPrompt(String userMessage) {
        List<Document> employeeDocs = employeeVectorStore.similaritySearch(
                SearchRequest.builder().query(userMessage).topK(2).build()
        );
        List<Document> positionDocs = positionVectorStore.similaritySearch(
                SearchRequest.builder().query(userMessage).topK(2).build()
        );
        List<Document> departmentDocs = departmentVectorStore.similaritySearch(
                SearchRequest.builder().query(userMessage).topK(2).build()
        );

        List<Document> allDocs = new ArrayList<>();
        if (employeeDocs != null) allDocs.addAll(employeeDocs);
        if (positionDocs != null) allDocs.addAll(positionDocs);
        if (departmentDocs != null) allDocs.addAll(departmentDocs);

        String context = allDocs.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n---\n"));

        SystemPromptTemplate promptTemplate = new SystemPromptTemplate(this.systemPromptTemplate);
        Message systemMessage = promptTemplate.createMessage(Map.of("context", context));

        return new Prompt(List.of(systemMessage, new UserMessage(userMessage)));
    }

}
