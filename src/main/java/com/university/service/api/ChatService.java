package com.university.service.api;

import com.university.dto.ChatResponse;
import com.university.dto.ChatUpdateRequest;
import com.university.dto.ChatUpdateResponse;

import java.util.List;

public interface ChatService {
    List<ChatResponse> getChats();

    void deleteChat(Integer chatId);

    ChatUpdateResponse updateChat(ChatUpdateRequest chatUpdateRequest);
}
