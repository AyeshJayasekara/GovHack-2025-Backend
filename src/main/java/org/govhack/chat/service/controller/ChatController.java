package org.govhack.chat.service.controller;

import org.govhack.chat.service.dto.ResponseDTO;
import org.govhack.chat.service.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;




@CrossOrigin(
        origins = "http://localhost:8081",   // your React dev origin
        allowedHeaders = "*",
        methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS },
        allowCredentials = "true",
        exposedHeaders = { "Content-Type" }
)
@RestController
public class ChatController {
    private final ChatService ragService;

    public ChatController(ChatService ragService) {
        this.ragService = ragService;
    }

    @PostMapping("/ask")
    public ResponseEntity<ResponseDTO> ask(@RequestBody String question,
                                      @RequestParam(defaultValue = "5") int k) {

        String answer = ragService.answer(question, k);
        return ResponseEntity.ok(new ResponseDTO(answer));

        //return ResponseEntity.ok(ragService.answer(question, k));
    }
}