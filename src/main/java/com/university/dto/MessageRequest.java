package com.university.dto;

import com.university.model.MessageType;

public record MessageRequest(
        String text,
        MessageType type,
        Integer chatId
) {
}
