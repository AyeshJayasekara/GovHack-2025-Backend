package org.govhack.chat.service.service;
import org.govhack.chat.service.tool.AuditTools;
import org.govhack.chat.service.tool.OutlierTool;
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
    private final AuditTools auditTools;

    public ChatService(VectorStore vectorStore, ChatModel chatModel, AuditTools auditTools) {
        this.vectorStore = vectorStore;
        this.chatModel = chatModel;
        this.auditTools = auditTools;
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

        // System Message
        var system = new SystemMessage(
                "You are a helpful assistant for a Government Agency. Your sole purpose is to " +
                        "answer the user's question only.  " +
                        "If retrieved context has a link to original data set be sure to include it as a reference."+
                        "Use only the provided context information to formulate your answer."
        + "Format your answer in Markdown format when responding.");

        var user = new UserMessage(
                "Question: " + question + "\n\nContext:\n" + context);

        return ChatClient.create(chatModel)
                .prompt(new Prompt(List.of(system, user)))
//                .tools(new AuditTools(), new OutlierTool())
                .call()
                .content();
    }
}