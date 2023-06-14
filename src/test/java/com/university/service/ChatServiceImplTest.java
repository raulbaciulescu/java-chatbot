package com.university.service;

import com.university.dto.ChatResponse;
import com.university.dto.ChatUpdateRequest;
import com.university.dto.ChatUpdateResponse;
import com.university.model.Chat;
import com.university.repository.ChatRepository;
import com.university.repository.MessageRepository;
import com.university.service.api.ChatService;
import com.university.service.api.MessagePdfService;
import com.university.service.api.MessageService;
import com.university.util.ChatResponseImpl;
import com.university.util.WithMockCustomUser;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
class ChatServiceImplTest {
    @Autowired
    private ChatService chatService;
    @MockBean
    private MessageRepository messageRepository;
    @MockBean
    private ChatRepository chatRepository;
    @MockBean
    private MessagePdfService messagePdfService;
    @MockBean
    private PythonServiceImpl pythonService;
    @Value("${python-chat-bot.normal-message}")
    private String url;

    @Test
    @WithMockCustomUser
    void getChats() {
        List<ChatResponse> chatResponses = List.of(
                new ChatResponseImpl("1", "chat1", "filename1"),
                new ChatResponseImpl("2", "chat2", "filename2"),
                new ChatResponseImpl("3", "chat3", "filename3")
        );
        when(chatRepository.findAllByUserId(1)).thenReturn(chatResponses);
        List<ChatResponse> computedList = chatService.getChats();
        assertEquals(chatResponses.get(0).getTitle(), computedList.get(0).getTitle());
        assertEquals(chatResponses.get(1).getTitle(), computedList.get(1).getTitle());
        assertEquals(chatResponses.get(2).getTitle(), computedList.get(2).getTitle());
    }

    @Test
    void deleteChat() {
        Chat c1 = new Chat();
        c1.setId(1);
        Chat c2 = new Chat();
        c2.setId(2);
        when(chatRepository.findById(1)).thenReturn(Optional.of(c2));
        chatService.deleteChat(1);
        verify(chatRepository, times(1)).deleteById(c1.getId());
    }

    @Test
    void updateChat() {
        ChatUpdateRequest chatUpdateRequest = new ChatUpdateRequest(1, "new title");
        ChatUpdateResponse chatUpdateResponse = new ChatUpdateResponse(1, "new title", "filename");
        Chat chat = new Chat();
        chat.setId(1);
        chat.setId(1);
        chat.setTitle("new title");

        Mockito.when(chatRepository.findById(chatUpdateRequest.id())).thenReturn(Optional.of(chat));
        Mockito.when(chatRepository.save(chat)).thenReturn(chat);
        ChatUpdateResponse chatUpdateResponse1 = chatService.updateChat(chatUpdateRequest);
        assertEquals(chatUpdateResponse1.id(), chatUpdateResponse.id());
        assertEquals(chatUpdateResponse1.title(), chatUpdateResponse.title());
    }
}