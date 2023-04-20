package com.university.controller;


import com.university.dto.MessageRequest;
import com.university.model.Message;
import com.university.repository.MessageRepository;
import com.university.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final MessageRepository messageRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<Message> saveMessage(@RequestBody MessageRequest messageRequest) {
        return messageRepository.findAll();
        //messageService.save(messageRequest);
    }

    @PostMapping("/da")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Message> saveMessage() {
        return messageRepository.findAll();
        //messageService.save(messageRequest);
    }
}
