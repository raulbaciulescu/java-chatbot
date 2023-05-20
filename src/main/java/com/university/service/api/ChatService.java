package com.university.service.api;

import com.university.dto.ChatResponse;

import java.util.List;

public interface ChatService {
    List<ChatResponse> getChats();
}
