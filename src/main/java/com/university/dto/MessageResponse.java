package com.university.dto;

import com.university.model.MessageType;

public record MessageResponse(String text, String title, MessageType type, Integer chatId, String filename) {
}
