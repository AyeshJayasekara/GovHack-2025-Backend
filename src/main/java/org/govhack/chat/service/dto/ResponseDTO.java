package org.govhack.chat.service.dto;

public class ResponseDTO {
    private String text;

    public ResponseDTO(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
