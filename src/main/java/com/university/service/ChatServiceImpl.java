package com.university.service;

import com.university.dto.ChatResponse;
import com.university.dto.ChatUpdateRequest;
import com.university.dto.ChatUpdateResponse;
import com.university.dto.DeleteChatRequest;
import com.university.exception.ResourceNotFoundException;
import com.university.model.Chat;
import com.university.model.User;
import com.university.repository.ChatRepository;
import com.university.repository.MessageRepository;
import com.university.service.api.ChatService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;

    @Override
    public List<ChatResponse> getChats() {
        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return chatRepository.findAllByUserId(loggedUser.getId());
    }

    @Override
    @Transactional
    public void deleteChat(Integer chatId) {
        messageRepository.deleteByChatId(chatId);
        chatRepository.deleteById(chatId);
    }

    @Override
    public ChatUpdateResponse updateChat(ChatUpdateRequest chatUpdateRequest) {
        Chat chat = chatRepository
                .findById(chatUpdateRequest.id())
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found!"));
        chat.setTitle(chatUpdateRequest.title());
        chatRepository.save(chat);
        return new ChatUpdateResponse(chat.getId(), chat.getTitle(), chat.getFilename());
    }
}
