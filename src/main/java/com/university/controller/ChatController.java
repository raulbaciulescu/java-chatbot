package com.university.controller;


import com.university.dto.ChatResponse;
import com.university.dto.ChatUpdateRequest;
import com.university.dto.ChatUpdateResponse;
import com.university.dto.DeleteChatRequest;
import com.university.model.Chat;
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

    @DeleteMapping("/{chatId}")
    public void deleteChat(@PathVariable Integer chatId) {
        chatService.deleteChat(chatId);
    }

    @PutMapping
    public ChatUpdateResponse updateChat(@RequestBody ChatUpdateRequest chatUpdateRequest) {
        return chatService.updateChat(chatUpdateRequest);
    }
}
