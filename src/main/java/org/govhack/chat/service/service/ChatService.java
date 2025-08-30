package org.govhack.chat.service.service;

import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final VectorStore vectorStore;
    private final ChatModel chatModel;

    public ChatService(VectorStore vectorStore, ChatModel chatModel) {
        this.vectorStore = vectorStore;
        this.chatModel = chatModel;
    }

    public String answer(String question, int topK) {
        var results = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(question)
                        .topK(topK)
                        .build());

        String context = results.stream()
                .map(d -> "- " + d.getText())
                .collect(Collectors.joining("\n"));

        var system = new SystemMessage(
                "You are a helpful assistant. Answer using only the provided context. " +
                        "If uncertain, say you don't know.");
        var user = new UserMessage(
                "Question: " + question + "\n\nContext:\n" + context);

        return chatModel.call(new Prompt(List.of(system, user))).getResult().getOutput().getText();
    }

}
