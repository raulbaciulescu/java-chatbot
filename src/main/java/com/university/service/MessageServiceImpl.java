package com.university.service;

import com.university.dto.MessageRequest;
import com.university.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;

    @Override
    public void save(MessageRequest messageRequest) {

    }
}
