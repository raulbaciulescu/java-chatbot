package com.university.service;

import com.university.dto.ChatResponse;
import com.university.repository.ChatRepository;
import com.university.service.api.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;

    @Override
    public List<ChatResponse> getChats() {
        return chatRepository.findAllBy();
    }
}
