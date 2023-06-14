package com.university.controller;


import com.university.dto.MessagePdfRequest;
import com.university.dto.MessageRequest;
import com.university.dto.MessageResponse;
import com.university.service.api.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponse saveMessage(@RequestBody MessageRequest messageRequest) {
        return messageService.save(messageRequest);
    }

    @GetMapping("/{chatId}/{page}")
    public List<MessageResponse> getMessages(@PathVariable Integer chatId, @PathVariable Integer page) {
        return messageService.getMessagesByChat(chatId, page);
    }

    @PostMapping("/pdf")
    public MessageResponse saveMessageWithPdf(@RequestPart MultipartFile file, @RequestPart String text) {
        return messageService.saveMessageWithPdf(new MessagePdfRequest(file, text));
    }
}
