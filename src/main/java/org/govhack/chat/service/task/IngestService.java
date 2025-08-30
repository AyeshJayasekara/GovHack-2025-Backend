package org.govhack.chat.service.task;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Service;

import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class IngestService {
    private final VectorStore vectorStore;
    private final TokenTextSplitter splitter = TokenTextSplitter.builder()
            .withChunkSize(512)
            .withMinChunkSizeChars(350)
            .build();

    public IngestService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public int ingestFolder(Path folder) throws Exception {
        List<Document> docs = new ArrayList<>();
        try (var paths = Files.walk(folder)) {
            for (Path p : paths.filter(Files::isRegularFile).collect(Collectors.toList())) {
                String content = Files.readString(p);
                Map<String, Object> meta = Map.of(
                        "filename", p.getFileName().toString(),
                        "path", p.toAbsolutePath().toString()
                );
                docs.add(new Document(content, meta));
            }
        }
        // split into chunks sized in tokens (works well with various models)
        List<Document> chunks = splitter.apply(docs);
        vectorStore.add(chunks);
        return chunks.size();
    }
}
