package com.university.controller;


import com.university.dto.ChatResponse;
import com.university.service.api.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @GetMapping
    public List<ChatResponse> getChats() {
        return chatService.getChats();
    }
}
