package org.govhack.chat.service.controller;

import org.govhack.chat.service.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ChatController {
    private final ChatService ragService;

    public ChatController(ChatService ragService) {
        this.ragService = ragService;
    }

    @PostMapping("/ask")
    public ResponseEntity<String> ask(@RequestBody String question,
                                      @RequestParam(defaultValue = "5") int k) {
        return ResponseEntity.ok(ragService.answer(question, k));
    }
}