package com.university.service.api;

import com.university.dto.MessagePdfRequest;
import com.university.dto.MessageRequest;
import com.university.dto.MessageResponse;
import com.university.model.Chat;

public interface MessagePdfService {
    MessageResponse saveMessageWithPdfFile(MessagePdfRequest request);

    MessageResponse sendMessageWithPdfFilename(MessageRequest request, Chat chat);

    void createAndSaveMessages(MessageRequest request, MessageResponse messageResponse, Chat chat);
}
