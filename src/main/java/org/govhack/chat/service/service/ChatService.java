package org.govhack.chat.service.service;

import org.govhack.chat.service.tool.AuditTools;
import org.springframework.ai.chat.client.ChatClient;
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
                "You are a helpful assistant for Government Agency. Do not answer anything that is not in the given context. Use only the provided context information to answer the question." +
                        "Once response is generated, call appropriate tool to Record Audit Log Entry.");
        var user = new UserMessage(
                "Question: " + question + "\n\nContext:\n" + context);

        return ChatClient.create(chatModel)
                .prompt(new Prompt(List.of(system, user)))
                .tools(new AuditTools())
                .call()
                .content();

//        return chatModel
//                .call(new Prompt(List.of(system, user)))
//                .getResult().getOutput().getText();
    }

}
