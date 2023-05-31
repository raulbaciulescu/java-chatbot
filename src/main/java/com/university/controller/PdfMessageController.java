package com.university.controller;

import com.university.dto.MessagePdfRequest;
import com.university.dto.MessageResponse;
import com.university.service.api.MessagePdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/pdf-messages")
@RequiredArgsConstructor
public class PdfMessageController {
    private final MessagePdfService messagePdfService;

    @PostMapping
    public MessageResponse saveMessageWithPdf(@RequestPart MultipartFile file, @RequestPart String text) {
         return messagePdfService.saveMessageWithPdfFile(new MessagePdfRequest(file, text));
    }
}
