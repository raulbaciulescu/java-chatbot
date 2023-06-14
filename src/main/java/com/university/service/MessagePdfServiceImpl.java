package com.university.service;

import com.university.dto.MessagePdfRequest;
import com.university.dto.MessageRequest;
import com.university.dto.MessageRequestToPythonWithFilename;
import com.university.dto.MessageResponse;
import com.university.model.Chat;
import com.university.model.Message;
import com.university.model.MessageType;
import com.university.model.User;
import com.university.repository.ChatRepository;
import com.university.repository.MessageRepository;
import com.university.service.api.MessagePdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MessagePdfServiceImpl implements MessagePdfService {
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final PythonServiceImpl pythonServiceImpl;
    @Value("${python-chat-bot.filename-message}")
    private String url;

    @Override
    public MessageResponse saveMessageWithPdfFile(MessagePdfRequest request) {
//        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        MessageResponse messageResponse = pythonServiceImpl.createPdfMessageWithFile(request);
//
//        Chat chat = new Chat();
//        chat.setTitle("Pdf chat");
//        chat.setUserId(loggedUser.getId());
//        chat.setFilename(request.file().getOriginalFilename());
//        chat = chatRepository.save(chat);
//        Message messageAi = Message.builder()
//                .chat(chat)
//                .type(MessageType.AI)
//                .text(messageResponse.text().trim())
//                .build();
//
//        Message messageUser = Message.builder()
//                .chat(chat)
//                .type(MessageType.USER)
//                .text(request.text())
//                .build();
//
//        messageRepository.save(messageUser);
//        messageRepository.save(messageAi);
//
//        return new MessageResponse(
//                messageResponse.text().trim(),
//                messageResponse.title(),
//                MessageType.AI,
//                chat.getId(),
//                chat.getFilename()
//        );
        return null;
    }

    @Override
    public MessageResponse sendMessageWithPdfFilename(MessageRequest request, Chat chat) {
        MessageResponse messageResponse = pythonServiceImpl.sendMessageToPython(
                new MessageRequestToPythonWithFilename(request.text(), chat.getFilename()),
                url
        );
        createAndSaveMessages(request, messageResponse, chat);
        return new MessageResponse(
                messageResponse.text().trim(),
                messageResponse.title(),
                MessageType.AI,
                chat.getId(),
                chat.getFilename()
        );
    }

    public void createAndSaveMessages(MessageRequest request, MessageResponse messageResponse, Chat chat) {
        Message messageAi = Message.builder()
                .chat(chat)
                .type(MessageType.AI)
                .text(messageResponse.text().trim())
                .build();

        Message messageUser = Message.builder()
                .chat(chat)
                .type(MessageType.USER)
                .text(request.text())
                .build();

        messageRepository.save(messageUser);
        messageRepository.save(messageAi);
    }
}
