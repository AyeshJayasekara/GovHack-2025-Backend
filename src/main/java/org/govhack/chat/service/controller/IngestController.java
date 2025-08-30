package org.govhack.chat.service.controller;

import org.govhack.chat.service.task.IngestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;

@RestController
@RequestMapping("/rag")
public class IngestController {
    private final IngestService ingestService;

    public IngestController(IngestService ingestService) {
        this.ingestService = ingestService;
    }

    @PostMapping("/ingest")
    public ResponseEntity<String> ingest() throws Exception {
        int n = ingestService.ingestFolder(Path.of("/Users/ayeshj/Desktop/GovHack-2025-Backend/DATA"));
        return ResponseEntity.ok("Indexed " + n + " chunks");
    }
}
