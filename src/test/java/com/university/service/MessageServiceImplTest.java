package com.university.service;

import com.university.dto.MessageRequest;
import com.university.dto.MessageRequestToPython;
import com.university.dto.MessageResponse;
import com.university.model.*;
import com.university.repository.ChatRepository;
import com.university.repository.MessageRepository;
import com.university.service.api.MessageService;
import com.university.util.WithMockCustomUser;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
class MessageServiceImplTest {
    @Autowired
    private MessageService messageService;
    @MockBean
    private MessageRepository messageRepository;
    @MockBean
    private ChatRepository chatRepository;
    @MockBean
    private PythonServiceImpl pythonService;
    @Value("${python-chat-bot.normal-message}")
    private String url;
    @Test
    void getMessagesByChat() {
        Pageable pageable = PageRequest.of(0, 5);
        Chat chat = new Chat();
        List<Message> messageList = List.of(
                new Message(1, chat, "message", MessageType.USER),
                new Message(2, chat, "message", MessageType.AI)
        );
        Page<Message> messagePage = new PageImpl<>(messageList, pageable, messageList.size());

        Mockito.when(messageRepository.findAllByChatIdOrderByIdDesc(1, pageable)).thenReturn(messagePage);
        List<MessageResponse> computedList = messageService.getMessagesByChat(1, 0);
        assertEquals(messageList.get(0).getText(), computedList.get(0).text());
        assertEquals(messageList.get(0).getType(), computedList.get(0).type());
        assertEquals(messageList.get(1).getText(), computedList.get(1).text());
        assertEquals(messageList.get(1).getType(), computedList.get(1).type());
    }

    @Test
    @WithMockCustomUser
    void save() {
        Chat chat = new Chat(1, "title", null, 1);
        MessageRequest messageRequest = new MessageRequest("message", MessageType.USER, 1);
        List<Message> messageList = List.of(
                new Message(1, chat, "message", MessageType.USER),
                new Message(2, chat, "message", MessageType.AI)
        );
        MessageResponse messageResponse = new MessageResponse("response", "", MessageType.AI, 1, null);
        Map<String, String> map = messageList.stream()
                .collect(Collectors.toMap(
                        message -> message.getType() == MessageType.USER ? "input" : "output",
                        Message::getText,
                        (existing, replacement) -> existing
                ));
        MessageRequestToPython messageRequestToPython = new MessageRequestToPython("message",  map.entrySet().stream().toList());

        Mockito.when(chatRepository.findById(1)).thenReturn(Optional.of(chat));
        Mockito.when(messageRepository.findByChatId(messageRequest.chatId())).thenReturn(messageList);
        Mockito.when(pythonService.sendMessageToPython(messageRequestToPython, url)).thenReturn(messageResponse);
        MessageResponse actualMessageResponse = messageService.save(messageRequest);
        assertEquals(actualMessageResponse.text(), messageResponse.text());
        assertEquals(actualMessageResponse.type(), messageResponse.type());
    }
}

