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

    @Value("classpath:prompts/rh-assistant-chat-prompt.st")
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

    public Flux<String> generateResponse(String userMessage) {

        List<Document> employeeDocs = employeeVectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(userMessage)
                        .topK(2)
                        .build()
        );
        List<Document> positionDocs = positionVectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(userMessage)
                        .topK(2)
                        .build()
        );
        List<Document> departmentDocs = departmentVectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(userMessage)
                        .topK(2)
                        .build()
        );

        List<Document> allDocs = new ArrayList<>();


        if(employeeDocs != null && !employeeDocs.isEmpty()) {
            allDocs.addAll(employeeDocs);
        }

        if(positionDocs != null && !positionDocs.isEmpty()) {
            allDocs.addAll(positionDocs);
        }

        if(departmentDocs != null && !departmentDocs.isEmpty()) {
            allDocs.addAll(departmentDocs);
        }


        String context = allDocs.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n---\n"));

        SystemPromptTemplate promptTemplate = new SystemPromptTemplate(this.systemPromptTemplate);
        Prompt prompt = promptTemplate.create(Map.of("context", context));

        var endPrompt = new Prompt((Message) List.of(prompt.getInstructions(), new UserMessage(userMessage)));

        return chatModel.stream(endPrompt)
                .map(chatResponse -> chatResponse.getResults().getFirst().toString());

    }

}
