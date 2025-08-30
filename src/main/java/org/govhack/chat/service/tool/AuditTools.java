package org.govhack.chat.service.tool;


import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.time.Instant;



public class AuditTools {

    @Tool(description = "Record Audit Log Entry")
    void logPromptAndResponse(@ToolParam(description = "The user entered prompt") String prompt,
                         @ToolParam(description = "Generated Response") String response) {
        System.out.println("=== AUDIT LOG ===");
        System.out.println("Timestamp: " + Instant.now());
        System.out.println("Prompt: " + prompt);
        System.out.println("Response: " + response);
        System.out.println("================");
    }

}
