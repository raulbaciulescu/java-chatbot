package com.university.service;

import com.university.dto.MessagePdfRequest;
import com.university.dto.MessageRequest;
import com.university.dto.MessageRequestToPythonWithFilename;
import com.university.dto.MessageResponse;
import com.university.exception.ResourceNotFoundException;
import com.university.model.Chat;
import com.university.model.FilenameChatId;
import com.university.model.Message;
import com.university.model.MessageType;
import com.university.repository.ChatRepository;
import com.university.repository.FilenameChatIdRepository;
import com.university.repository.MessageRepository;
import com.university.service.api.MessagePdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MessagePdfServiceImpl implements MessagePdfService {
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final PythonService pythonService;
    private final FilenameChatIdRepository filenameChatIdRepository;

    @Override
    public MessageResponse saveMessageWithPdfFile(MessagePdfRequest request) {
        MessageResponse messageResponse = pythonService.createPdfMessageWithFile(request);

        Chat chat = new Chat();
        chat.setTitle("Pdf chat");
        chat.setFilename(request.file().getOriginalFilename());
        chat = chatRepository.save(chat);
        Message messageAi = Message.builder()
                .chat(chat)
                .type(MessageType.AI)
                .text(messageResponse.text())
                .build();

        Message messageUser = Message.builder()
                .chat(chat)
                .type(MessageType.USER)
                .text(request.text())
                .build();

        messageRepository.save(messageUser);
        messageRepository.save(messageAi);

        return new MessageResponse(messageResponse.text(),
                messageResponse.title(),
                MessageType.AI,
                chat.getId(),
                chat.getFilename()
        );
    }

    @Override
    public MessageResponse sendMessageWithPdfFilename(MessageRequest request, Chat chat) {
        MessageResponse messageResponse = pythonService.createPdfMessageWithFilename(
                new MessageRequestToPythonWithFilename(request.text(), chat.getFilename())
        );

        Message messageAi = Message.builder()
                .chat(chat)
                .type(MessageType.AI)
                .text(messageResponse.text())
                .build();

        Message messageUser = Message.builder()
                .chat(chat)
                .type(MessageType.USER)
                .text(request.text())
                .build();

        messageRepository.save(messageUser);
        messageRepository.save(messageAi);

        return new MessageResponse(messageResponse.text(),
                messageResponse.title(),
                MessageType.AI,
                chat.getId(),
                chat.getFilename()
        );
    }
}
