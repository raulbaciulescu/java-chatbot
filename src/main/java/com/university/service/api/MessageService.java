package com.university.service.api;

import com.university.dto.MessageRequest;
import com.university.dto.MessageResponse;
import com.university.model.Chat;

import java.util.List;

public interface MessageService {
    MessageResponse save(MessageRequest messageRequest);

    List<MessageResponse> getMessagesByChat(Integer chatId, Integer page);
}
