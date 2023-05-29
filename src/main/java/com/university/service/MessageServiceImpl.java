package com.university.service;

import com.university.dto.MessageRequest;
import com.university.dto.MessageRequestToPython;
import com.university.dto.MessageResponse;
import com.university.exception.ResourceNotFoundException;
import com.university.model.Chat;
import com.university.model.Message;
import com.university.model.MessageType;
import com.university.repository.ChatRepository;
import com.university.repository.MessageRepository;
import com.university.service.api.MessagePdfService;
import com.university.service.api.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final PythonService pythonService;
    private final MessagePdfService messagePdfService;

    @Override
    public List<MessageResponse> getMessagesByChat(Integer chatId, Integer page) {
        Pageable pageable = PageRequest.of(page, 5);
        return messageRepository.findAllByChatIdOrderByIdDesc(chatId, pageable)
                .stream()
                .map(m -> new MessageResponse(m.getText(), "", m.getType(), chatId, ""))
                .toList();
    }

    @Override
    public MessageResponse save(MessageRequest messageRequest) {
        Chat chat = getChat(messageRequest.chatId());
        if (chat.getFilename() != null)
            return messagePdfService.sendMessageWithPdfFilename(messageRequest, chat);
        else
            return sendNormalMessage(messageRequest, chat);
    }

    private MessageResponse sendNormalMessage(MessageRequest messageRequest, Chat chat) {
        MessageResponse messageResponse = buildAndSendMessageToPython(messageRequest);
        Message messageAi = Message.builder()
                .chat(chat)
                .type(MessageType.AI)
                .text(messageResponse.text())
                .build();

        Message messageUser = Message.builder()
                .chat(chat)
                .type(MessageType.USER)
                .text(messageRequest.text())
                .build();

        messageRepository.save(messageUser);
        messageRepository.save(messageAi);

        return new MessageResponse(messageResponse.text(),
                chat.getTitle(),
                MessageType.AI,
                chat.getId(),
                chat.getFilename()
        );
    }

    private Chat getChat(Integer chatId) {
        Chat chat;
        if (chatId == 0) {
            chat = new Chat();
            chat.setTitle("Normal chat");
            chatRepository.save(chat);
        } else {
            chat = chatRepository.findById(chatId).orElseThrow(() -> new ResourceNotFoundException("This chat doesn't exist"));
        }

        return chat;
    }

    private MessageResponse buildAndSendMessageToPython(MessageRequest messageRequest) {
        List<Message> messages = messageRepository.findByChatId(messageRequest.chatId());
        Map<String, String> map = messages.stream()
                .collect(Collectors.toMap(
                        message -> message.getType() == MessageType.USER ? "input" : "output",
                        Message::getText,
                        (existing, replacement) -> existing
                ));

        MessageRequestToPython messageRequestToPython = new MessageRequestToPython(
                messageRequest.text(),
                map.entrySet().stream().toList(),
                messageRequest.chatId() == 0
        );
        return pythonService.createMessage(messageRequestToPython);
    }
}